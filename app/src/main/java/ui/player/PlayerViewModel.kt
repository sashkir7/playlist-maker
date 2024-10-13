package ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.player.FavoriteInteractor
import domain.player.PlayerInteractor
import domain.player.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.player.PlayerState.Default
import ui.player.PlayerState.Favorite
import ui.player.PlayerState.Paused
import ui.player.PlayerState.Playing
import ui.player.PlayerState.Prepared

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoriteInteractor: FavoriteInteractor
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
        playerInteractor.prepare(
            previewUrl = previewUrl,
            onPrepareListener = { mutableState.postValue(Prepared) },
            onCompletionListener = {
                mutableState.postValue(Prepared)
                stopTimer()
            }
        )
    }

    fun start() {
        playerInteractor.start()
        mutableState.postValue(Playing(playerInteractor.currentPosition()))
        startTimer()
    }

    fun pause() {
        playerInteractor.pause()
        stopTimer()
        mutableState.postValue(Paused)
    }

    fun release() = playerInteractor.release()

    fun onFavoriteClicked(track: Track) = viewModelScope.launch {
        val favoriteTrack = favoriteInteractor.getById(track.trackId)
        if (favoriteTrack == null) {
            favoriteInteractor.add(track)
            mutableState.postValue(Favorite(isFavorite = true))
        } else {
            favoriteInteractor.delete(track)
            mutableState.postValue(Favorite(isFavorite = false))
        }
    }

    fun init(track: Track) = viewModelScope.launch {
        val favoriteTrack = favoriteInteractor.getById(track.trackId)
        if (favoriteTrack == null) {
            mutableState.postValue(Favorite(isFavorite = false))
        } else {
            mutableState.postValue(Favorite(isFavorite = true))
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {
                delay(TRACK_CURRENT_POSITION_DELAY)
                mutableState.postValue(Playing(playerInteractor.currentPosition()))
            }
        }
    }

    private fun stopTimer() = timerJob?.cancel()
}
