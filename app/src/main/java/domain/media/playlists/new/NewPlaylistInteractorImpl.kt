package domain.media.playlists.new

import android.net.Uri
import data.media.playlists.new.PlaylistRepository
import domain.media.Playlist

class NewPlaylistInteractorImpl(
    private val repository: PlaylistRepository
) : NewPlaylistInteractor {

    override suspend fun add(playlist: Playlist) =
        repository.add(playlist)

    override fun saveImage(uri: Uri, image: String): String =
        repository.saveImage(uri, image)

}
