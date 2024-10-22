package domain.media.playlists.new

import android.net.Uri
import domain.media.Playlist

interface NewPlaylistInteractor {

    suspend fun add(playlist: Playlist)

    fun saveImage(uri: Uri, image: String): String

}
