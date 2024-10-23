package data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_tracks")
data class PlaylistTrackEntity(
    @PrimaryKey
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val collectionName: String?,
    val releaseDate: Long?,
    val genreName: String?,
    val country: String?,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val previewUrl: String
)
