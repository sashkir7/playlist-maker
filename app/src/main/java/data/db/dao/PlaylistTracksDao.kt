package data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import data.db.entity.PlaylistTrackEntity

@Dao
interface PlaylistTracksDao {

    @Insert(onConflict = IGNORE)
    suspend fun add(track: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_tracks WHERE trackId = :id")
    suspend fun getById(id: Int): PlaylistTrackEntity?

}
