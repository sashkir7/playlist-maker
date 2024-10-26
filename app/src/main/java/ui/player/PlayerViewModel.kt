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
import ui.player.PlayerState.Paused
import ui.player.PlayerState.Playing
import ui.player.PlayerState.Prepared
import ui.player.PlayerTrackState.AddedToPlaylist
import ui.player.PlayerTrackState.AlreadyInPlaylist
import ui.player.PlayerTrackState.InFavorite
import ui.player.PlayerTrackState.NotInFavorite
import ui.player.PlayerTrackState.ReceivedPlaylists

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoriteInteractor: FavoriteInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    companion object {
        private const val TRACK_CURRENT_POSITION_DELAY = 300L
    }

    private var timerJob: Job? = null

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> get() = _playerState

    private val _trackState = MutableLiveData<PlayerTrackState>()
    val trackState: LiveData<PlayerTrackState> get() = _trackState

    init {
        _playerState.postValue(Default)
    }

    fun prepare(previewUrl: String) {
        playerInteractor.prepare(
            previewUrl = previewUrl,
            onPrepareListener = { _playerState.postValue(Prepared) },
            onCompletionListener = {
                _playerState.postValue(Prepared)
                stopTimer()
            }
        )
    }

    fun start() {
        playerInteractor.start()
        _playerState.postValue(Playing(playerInteractor.currentPosition()))
        startTimer()
    }

    fun pause() {
        playerInteractor.pause()
        stopTimer()
        _playerState.postValue(Paused)
    }

    fun release() = playerInteractor.release()

    fun onFavoriteClicked(track: Track) = viewModelScope.launch {
        val favoriteTrack = favoriteInteractor.getById(track.trackId)
        if (favoriteTrack == null) {
            favoriteInteractor.add(track)
            _trackState.postValue(InFavorite)
        } else {
            favoriteInteractor.delete(track)
            _trackState.postValue(NotInFavorite)
        }
    }

    fun init(track: Track) = viewModelScope.launch {
        val favoriteTrack = favoriteInteractor.getById(track.trackId)
        if (favoriteTrack == null) {
            _trackState.postValue(NotInFavorite)
        } else {
            _trackState.postValue(InFavorite)
        }

        getPlaylists()
    }

    fun addToPlaylist(playlistId: Int, track: Track) = viewModelScope.launch {
        val playlist = playlistInteractor.getById(playlistId)
        if (playlist.containsTrack(track)) {
            _trackState.postValue(AlreadyInPlaylist(playlist))
        } else {
            playlist.addTrack(track)
            playlistInteractor.update(playlist)
            playlistInteractor.addPlaylistTrack(track)
            _trackState.postValue(AddedToPlaylist(playlist))

            getPlaylists()
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {
                delay(TRACK_CURRENT_POSITION_DELAY)
                _playerState.postValue(Playing(playerInteractor.currentPosition()))
            }
        }
    }

    private fun getPlaylists() = viewModelScope.launch {
        playlistInteractor.getAll()
            .collect { playlists -> _trackState.postValue(ReceivedPlaylists(playlists)) }
    }

    private fun stopTimer() = timerJob?.cancel()
}
