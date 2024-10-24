package data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tracks")
data class TrackEntity(
    @PrimaryKey
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val collectionName: String?,
    val releaseDate: Long?,
    val genreName: String?,
    val country: String?,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val previewUrl: String,
    val timestamp: Long
)
