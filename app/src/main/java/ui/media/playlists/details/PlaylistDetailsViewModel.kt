package ui.media.playlists.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.media.PlaylistInteractor
import domain.player.Track
import kotlinx.coroutines.launch
import ui.media.playlists.details.PlaylistDetailsState.ReceivedPlaylist
import ui.media.playlists.details.PlaylistDetailsState.ReceivedTracks

class PlaylistDetailsViewModel(
    private val interactor: PlaylistInteractor
) : ViewModel() {

    private val _state = MutableLiveData<PlaylistDetailsState>()
    val state: LiveData<PlaylistDetailsState> get() = _state

    fun getPlaylist(playlistId: Int) = viewModelScope.launch {
        val playlist = interactor.getById(playlistId)
        _state.postValue(ReceivedPlaylist(playlist))
    }

    fun getTracksInPlaylist(playlistId: Int) = viewModelScope.launch {
        val playlist = interactor.getById(playlistId)
        val tracks = interactor.getPlaylistTracks(playlist)
        _state.postValue(ReceivedTracks(tracks))
    }

    fun deleteTrackFromPlaylist(
        playlistId: Int,
        track: Track
    ) = viewModelScope.launch {
        interactor.deleteTrackFromPlaylist(interactor.getById(playlistId), track)
        getPlaylist(playlistId)
        getTracksInPlaylist(playlistId)
    }
}
