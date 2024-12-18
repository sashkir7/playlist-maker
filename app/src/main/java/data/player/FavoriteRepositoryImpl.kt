package data.player

import data.convertors.FavoriteTrackDbConvertor
import data.db.dao.FavoriteTracksDao
import domain.player.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val favoriteDao: FavoriteTracksDao,
    private val convertor: FavoriteTrackDbConvertor
) : FavoriteRepository {

    override suspend fun add(track: Track) =
        favoriteDao.add(convertor.map(track))

    override suspend fun getById(id: Long): Track? {
        val entity = favoriteDao.getById(id) ?: return null
        return convertor.map(entity)
    }

    override suspend fun delete(track: Track) =
        favoriteDao.delete(convertor.map(track))

    override fun getAll(): Flow<List<Track>> = flow {
        val tracks = favoriteDao.getAll()
        emit(tracks.map { convertor.map(it) })
    }
}
