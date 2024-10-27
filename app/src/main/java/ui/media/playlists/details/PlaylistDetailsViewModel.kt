package ui.media.playlists.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.media.PlaylistInteractor
import domain.player.Track
import domain.sharing.SharingInteractor
import kotlinx.coroutines.launch
import ui.media.playlists.details.PlaylistDetailsState.DeletedPlaylist
import ui.media.playlists.details.PlaylistDetailsState.ReceivedPlaylist
import ui.media.playlists.details.PlaylistDetailsState.SharedEmptyPlaylist
import utils.EndingConvertor

class PlaylistDetailsViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val _playlistState = MutableLiveData<PlaylistDetailsState>()
    val playlistState: LiveData<PlaylistDetailsState> get() = _playlistState

    private val _tracksState = MutableLiveData<List<Track>>()
    val tracksState: LiveData<List<Track>> get() = _tracksState

    fun getPlaylist(playlistId: Int) = viewModelScope.launch {
        val playlist = playlistInteractor.getById(playlistId)
        _playlistState.postValue(ReceivedPlaylist(playlist))
    }

    fun getTracksInPlaylist(playlistId: Int) = viewModelScope.launch {
        val playlist = playlistInteractor.getById(playlistId)
        val tracks = playlistInteractor.getPlaylistTracks(playlist)
        _tracksState.postValue(tracks)
    }

    fun deleteTrackFromPlaylist(
        playlistId: Int,
        track: Track
    ) = viewModelScope.launch {
        playlistInteractor.deleteTrackFromPlaylist(playlistInteractor.getById(playlistId), track)
        getPlaylist(playlistId)
        getTracksInPlaylist(playlistId)
    }

    fun share(playlistId: Int) = viewModelScope.launch {
        val playlist = playlistInteractor.getById(playlistId)
        val tracks = playlistInteractor.getPlaylistTracks(playlist)

        if (tracks.isEmpty()) {
            _playlistState.postValue(SharedEmptyPlaylist)
        } else {
            var message = "${playlist.name}\n"
            if (!playlist.description.isNullOrBlank()) message += "${playlist.description}\n"
            message += "${EndingConvertor.track(tracks.size)}\n"
            tracks.forEachIndexed { index, track ->
                message += "${index + 1}. ${track.artistName} - ${track.trackName} (${track.trackTimeAsString})"
                if (index != tracks.size - 1) message += "\n"
            }

            sharingInteractor.shareApp(message, playlist.name)
        }
    }

    fun deletePlaylist(playlistId: Int) = viewModelScope.launch {
        playlistInteractor.delete(playlistId)
        _playlistState.postValue(DeletedPlaylist)
    }
}
