package data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import data.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {

    @Insert
    suspend fun add(playlist: PlaylistEntity)

    @Update(onConflict = REPLACE)
    suspend fun update(playlist: PlaylistEntity)

    @Delete
    suspend fun delete(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist WHERE id = :id")
    suspend fun getById(id: Int): PlaylistEntity

    @Query("SELECT * FROM playlist ORDER BY name ASC")
    suspend fun getAll(): List<PlaylistEntity>

}
