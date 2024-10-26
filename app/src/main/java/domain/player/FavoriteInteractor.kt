package domain.player

import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {

    suspend fun add(track: Track)

    suspend fun getById(id: Long): Track?

    suspend fun delete(track: Track)

    fun getAll(): Flow<List<Track>>

}
