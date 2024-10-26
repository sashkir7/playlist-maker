package domain.media

import android.net.Uri
import data.media.playlists.PlaylistRepository
import domain.player.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val repository: PlaylistRepository
) : PlaylistInteractor {

    override suspend fun add(playlist: Playlist) =
        repository.add(playlist)

    override suspend fun addPlaylistTrack(track: Track) =
        repository.addPlaylistTrack(track)

    override suspend fun update(playlist: Playlist) =
        repository.update(playlist)

    override suspend fun getById(id: Int): Playlist =
        repository.getById(id)

    override fun getAll(): Flow<List<Playlist>> =
        repository.getAll()

    override fun saveImage(uri: Uri, image: String): String =
        repository.saveImage(uri, image)
}
