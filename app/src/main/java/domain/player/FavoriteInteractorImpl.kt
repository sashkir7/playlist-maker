package domain.player

import data.player.FavoriteRepository
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(
    private val repository: FavoriteRepository
) : FavoriteInteractor {

    override suspend fun add(track: Track) = repository.add(track)

    override suspend fun getById(id: Int): Track? = repository.getById(id)

    override suspend fun delete(track: Track) = repository.delete(track)

    override fun getAll(): Flow<List<Track>> = repository.getAll()

}