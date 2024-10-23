package ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.media.PlaylistInteractor
import domain.player.FavoriteInteractor
import domain.player.PlayerInteractor
import domain.player.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.player.PlayerState.Default
import ui.player.PlayerState.Favorite
import ui.player.PlayerState.GetPlaylists
import ui.player.PlayerState.Paused
import ui.player.PlayerState.Playing
import ui.player.PlayerState.Prepared

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoriteInteractor: FavoriteInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    companion object {
        private const val TRACK_CURRENT_POSITION_DELAY = 300L
    }

    private var timerJob: Job? = null

    private val _state = MutableLiveData<PlayerState>()
    val state: LiveData<PlayerState> get() = _state

    init {
        _state.postValue(Default)
    }

    fun prepare(previewUrl: String) {
        playerInteractor.prepare(
            previewUrl = previewUrl,
            onPrepareListener = { _state.postValue(Prepared) },
            onCompletionListener = {
                _state.postValue(Prepared)
                stopTimer()
            }
        )
    }

    fun start() {
        playerInteractor.start()
        _state.postValue(Playing(playerInteractor.currentPosition()))
        startTimer()
    }

    fun pause() {
        playerInteractor.pause()
        stopTimer()
        _state.postValue(Paused)
    }

    fun release() = playerInteractor.release()

    fun onFavoriteClicked(track: Track) = viewModelScope.launch {
        val favoriteTrack = favoriteInteractor.getById(track.trackId)
        if (favoriteTrack == null) {
            favoriteInteractor.add(track)
            _state.postValue(Favorite(isFavorite = true))
        } else {
            favoriteInteractor.delete(track)
            _state.postValue(Favorite(isFavorite = false))
        }
    }

    fun init(track: Track) = viewModelScope.launch {
        val favoriteTrack = favoriteInteractor.getById(track.trackId)
        if (favoriteTrack == null) {
            _state.postValue(Favorite(isFavorite = false))
        } else {
            _state.postValue(Favorite(isFavorite = true))
        }
    }

    fun getPlaylists() = viewModelScope.launch {
        playlistInteractor.getAll()
            .collect { playlists -> _state.postValue(GetPlaylists(playlists)) }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {
                delay(TRACK_CURRENT_POSITION_DELAY)
                _state.postValue(Playing(playerInteractor.currentPosition()))
            }
        }
    }

    private fun stopTimer() = timerJob?.cancel()
}
