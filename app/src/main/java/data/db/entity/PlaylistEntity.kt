package data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import data.convertors.CustomConvertors

@Entity(tableName = "playlist")
@TypeConverters(value = [CustomConvertors::class])
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String,
    val description: String?,
    val pathToImage: String?,
    val trackIds: List<Long>,
    val timestamp: Long
)
