package data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import data.db.dao.FavoriteTracksDao
import data.db.entity.TrackEntity

@Database(
    version = 1,
    entities = [
        TrackEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteTracksDao(): FavoriteTracksDao
}
