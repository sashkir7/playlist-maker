package ui.media.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.media.Playlist
import domain.media.PlaylistInteractor
import kotlinx.coroutines.launch
import ui.media.playlists.PlaylistsState.Content
import ui.media.playlists.PlaylistsState.Empty

class PlaylistsViewModel(
    private val interactor: PlaylistInteractor
) : ViewModel() {

    private val _state = MutableLiveData<PlaylistsState>()
    val state: LiveData<PlaylistsState> get() = _state

    fun fillData() = viewModelScope.launch {
        interactor.getAll().collect { playlists -> processResult(playlists) }
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            _state.postValue(Empty)
        } else {
            _state.postValue(Content(playlists))
        }
    }
}
