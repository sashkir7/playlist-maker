package ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.player.PlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.player.PlayerState.Default
import ui.player.PlayerState.Paused
import ui.player.PlayerState.Playing
import ui.player.PlayerState.Prepared

class PlayerViewModel(
    private val interactor: PlayerInteractor
) : ViewModel() {

    companion object {
        private const val TRACK_CURRENT_POSITION_DELAY = 300L
    }

    private var timerJob: Job? = null

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
                stopTimer()
            }
        )
    }

    fun start() {
        interactor.start()
        mutableState.postValue(Playing(interactor.currentPosition()))
        startTimer()
    }

    fun pause() {
        interactor.pause()
        stopTimer()
        mutableState.postValue(Paused)
    }

    fun release() = interactor.release()

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (interactor.isPlaying()) {
                delay(TRACK_CURRENT_POSITION_DELAY)
                mutableState.postValue(Playing(interactor.currentPosition()))
            }
        }
    }

    private fun stopTimer() = timerJob?.cancel()
}
