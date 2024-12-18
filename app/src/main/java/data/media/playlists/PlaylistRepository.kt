package data.media.playlists

import domain.media.Playlist
import domain.player.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun add(playlist: Playlist)

    suspend fun addPlaylistTrack(track: Track)

    suspend fun deleteTrackFromPlaylist(playlist: Playlist, track: Track)

    suspend fun update(playlist: Playlist)

    suspend fun delete(playlistId: Int)

    suspend fun getById(id: Int): Playlist

    suspend fun getPlaylistTracks(playlist: Playlist): List<Track>

    fun getAll(): Flow<List<Playlist>>

    fun saveImage(uri: String, image: String): String

}
