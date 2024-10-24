package data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import data.db.dao.FavoriteTracksDao
import data.db.dao.PlaylistDao
import data.db.dao.PlaylistTracksDao
import data.db.entity.FavoriteTrackEntity
import data.db.entity.PlaylistEntity
import data.db.entity.PlaylistTrackEntity

@Database(
    version = 1,
    entities = [
        PlaylistEntity::class,
        PlaylistTrackEntity::class,
        FavoriteTrackEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun playlistDao(): PlaylistDao

    abstract fun playlistTracksDao(): PlaylistTracksDao

    abstract fun favoriteTracksDao(): FavoriteTracksDao
}
