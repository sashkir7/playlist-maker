package ui.media.playlists.modify

import android.net.Uri
import androidx.lifecycle.ViewModel
import domain.media.PlaylistInteractor

abstract class ModifyPlaylistViewModel(
    protected val interactor: PlaylistInteractor
) : ViewModel() {

    fun saveImage(uri: Uri, image: String): String =
        interactor.saveImage(uri, image)

}
