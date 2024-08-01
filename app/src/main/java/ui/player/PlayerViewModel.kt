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
import com.example.playlistmaker.R
import creator.Creator
import domain.player.MediaPlayerState
import domain.player.MediaPlayerState.DEFAULT
import domain.player.MediaPlayerState.PAUSED
import domain.player.MediaPlayerState.PLAYING
import domain.player.MediaPlayerState.PREPARED
import domain.player.PlayerInteractor

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
            mutableCurrentPosition.postValue(interactor.currentPosition())
            mainThreadHandler.postDelayed(this, TRACK_CURRENT_POSITION_DELAY)
        }
    }

    private val mutableCurrentPosition =
        MutableLiveData(application.getString(R.string.track_duration_zero))
    val currentPosition: LiveData<String>
        get() = mutableCurrentPosition

    private val mutablePlayerState = MutableLiveData(DEFAULT)
    val playerState: LiveData<MediaPlayerState>
        get() = mutablePlayerState

    fun prepare(previewUrl: String) {
        interactor.prepare(
            previewUrl = previewUrl,
            onPrepareListener = { mutablePlayerState.postValue(PREPARED) },
            onCompletionListener = {
                mutablePlayerState.postValue(PREPARED)
                mutableCurrentPosition.postValue(application.getString(R.string.track_duration_zero))
                mainThreadHandler.removeCallbacks(updateCurrentPositionRunnable)
            }
        )
    }

    fun start() {
        interactor.start()
        mutablePlayerState.postValue(PLAYING)
        mainThreadHandler.post(updateCurrentPositionRunnable)
    }

    fun pause() {
        interactor.pause()
        mutablePlayerState.postValue(PAUSED)
        mainThreadHandler.removeCallbacks(updateCurrentPositionRunnable)
    }

    fun release() = interactor.release()
}
