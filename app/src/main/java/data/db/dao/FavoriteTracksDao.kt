package data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import data.db.entity.TrackEntity

@Dao
interface FavoriteTracksDao {

    @Insert
    suspend fun add(track: TrackEntity)

    @Query("SELECT * FROM favorite_tracks WHERE trackId = :id")
    suspend fun getById(id: Int): TrackEntity?

    @Delete
    suspend fun delete(track: TrackEntity)

    @Query("SELECT * FROM favorite_tracks ORDER BY timestamp DESC")
    suspend fun getAll(): List<TrackEntity>

}
