package data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import data.db.entity.FavoriteTrackEntity

@Dao
interface FavoriteTracksDao {

    @Insert
    suspend fun add(track: FavoriteTrackEntity)

    @Query("SELECT * FROM favorite_tracks WHERE trackId = :id")
    suspend fun getById(id: Long): FavoriteTrackEntity?

    @Delete
    suspend fun delete(track: FavoriteTrackEntity)

    @Query("SELECT * FROM favorite_tracks ORDER BY timestamp DESC")
    suspend fun getAll(): List<FavoriteTrackEntity>

}
