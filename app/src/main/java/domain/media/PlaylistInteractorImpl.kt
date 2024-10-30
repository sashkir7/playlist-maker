package domain.media

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

    override suspend fun deleteTrackFromPlaylist(playlist: Playlist, track: Track) =
        repository.deleteTrackFromPlaylist(playlist, track)

    override suspend fun update(playlist: Playlist) =
        repository.update(playlist)

    override suspend fun delete(playlistId: Int) =
        repository.delete(playlistId)

    override suspend fun getById(id: Int): Playlist =
        repository.getById(id)

    override suspend fun getPlaylistTracks(playlist: Playlist): List<Track> =
        repository.getPlaylistTracks(playlist)

    override fun getAll(): Flow<List<Playlist>> =
        repository.getAll()

    override fun saveImage(uri: String, image: String): String =
        repository.saveImage(uri, image)
}
