package ui.media.playlists.new

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.media.Playlist
import domain.media.playlists.new.NewPlaylistInteractor
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val interactor: NewPlaylistInteractor
) : ViewModel() {

    fun createPlaylist(playlist: Playlist) =
        viewModelScope.launch { interactor.add(playlist) }

    fun saveImage(uri: Uri, image: String): String =
        interactor.saveImage(uri, image)

}
