package data.media.playlists.new

import android.net.Uri
import domain.media.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun add(playlist: Playlist)

    fun getAll(): Flow<List<Playlist>>

    fun saveImage(uri: Uri, image: String): String

}
