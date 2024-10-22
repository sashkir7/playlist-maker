package data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import data.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {

    @Insert
    suspend fun add(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist ORDER BY timestamp DESC")
    suspend fun getAll(): List<PlaylistEntity>

}
