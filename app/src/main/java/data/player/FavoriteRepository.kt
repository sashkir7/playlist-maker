package data.player

import domain.player.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    suspend fun add(track: Track)

    suspend fun getById(id: Int): Track?

    suspend fun delete(track: Track)

    fun getAll(): Flow<List<Track>>

}
