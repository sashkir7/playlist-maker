package ui.media.playlists.modify.create

import androidx.lifecycle.viewModelScope
import domain.media.Playlist
import domain.media.PlaylistInteractor
import kotlinx.coroutines.launch
import ui.media.playlists.modify.ModifyPlaylistViewModel

class CreatePlaylistViewModel(
    interactor: PlaylistInteractor
) : ModifyPlaylistViewModel(interactor) {

    fun createPlaylist(playlist: Playlist) =
        viewModelScope.launch { interactor.add(playlist) }

}
