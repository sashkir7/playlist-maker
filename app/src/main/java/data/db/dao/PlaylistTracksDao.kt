package data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import data.db.entity.PlaylistTrackEntity

@Dao
interface PlaylistTracksDao {

    @Insert
    suspend fun add(track: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_tracks WHERE trackId = :id")
    suspend fun getById(id: Int): PlaylistTrackEntity?

}
