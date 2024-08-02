package ui.player

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import creator.Creator
import domain.player.PlayerInteractor
import ui.player.PlayerState.Default
import ui.player.PlayerState.Paused
import ui.player.PlayerState.Playing
import ui.player.PlayerState.Prepared

class PlayerViewModel(
    private val application: Application,
    private val interactor: PlayerInteractor
) : AndroidViewModel(application) {

    companion object {
        private const val TRACK_CURRENT_POSITION_DELAY = 250L
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(
                    application = this[APPLICATION_KEY] as Application,
                    interactor = Creator.providePlayerInteractor()
                )
            }
        }
    }

    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private val updateCurrentPositionRunnable = object : Runnable {
        override fun run() {
            mutableState.postValue(Playing(interactor.currentPosition()))
            mainThreadHandler.postDelayed(this, TRACK_CURRENT_POSITION_DELAY)
        }
    }

    private val mutableState = MutableLiveData<PlayerState>()
    val state: LiveData<PlayerState>
        get() = mutableState

    init {
        mutableState.postValue(Default)
    }

    fun prepare(previewUrl: String) {
        interactor.prepare(
            previewUrl = previewUrl,
            onPrepareListener = { mutableState.postValue(Prepared) },
            onCompletionListener = {
                mutableState.postValue(Prepared)
                mainThreadHandler.removeCallbacks(updateCurrentPositionRunnable)
            }
        )
    }

    fun start() {
        interactor.start()
        mutableState.postValue(Playing(interactor.currentPosition()))
        mainThreadHandler.post(updateCurrentPositionRunnable)
    }

    fun pause() {
        interactor.pause()
        mutableState.postValue(Paused)
        mainThreadHandler.removeCallbacks(updateCurrentPositionRunnable)
    }

    fun release() = interactor.release()
}
