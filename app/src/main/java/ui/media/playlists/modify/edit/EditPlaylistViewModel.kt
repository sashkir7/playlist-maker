package ui.media.playlists.modify.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import domain.media.Playlist
import domain.media.PlaylistInteractor
import kotlinx.coroutines.launch
import ui.media.playlists.modify.ModifyPlaylistViewModel

class EditPlaylistViewModel(
    interactor: PlaylistInteractor
) : ModifyPlaylistViewModel(interactor) {

    private val _state = MutableLiveData<Playlist>()
    val state: LiveData<Playlist> get() = _state

    fun getPlaylist(playlistId: Int) = viewModelScope.launch {
        _state.postValue(interactor.getById(playlistId))
    }

    fun updatePlaylist(
        name: String,
        description: String?,
        pathToImage: String?
    ) = viewModelScope.launch {
        val currentPlaylist = state.value!!
        val updatedPlaylist = currentPlaylist.copy(
            name = name,
            description = description,
            pathToImage = pathToImage ?: currentPlaylist.pathToImage
        )
        interactor.update(updatedPlaylist)
    }
}
