package api.dtos

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class TrackDto(
    @SerializedName("trackId") val trackId: Int,
    @SerializedName("trackName") val trackName: String,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("collectionName") val collectionName: String?,
    @SerializedName("releaseDate") val releaseDate: Date?,
    @SerializedName("primaryGenreName") val genreName: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("trackTimeMillis") val trackTimeMillis: Long,
    @SerializedName("artworkUrl100") val artworkUrl100: String,
    @SerializedName("previewUrl") val previewUrl: String
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return trackId == (other as TrackDto).trackId
    }

    override fun hashCode(): Int = trackId
}

val TrackDto.trackTimeAsString: String
    get() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)

val TrackDto.releaseDateYear: Int?
    get() {
        val date = releaseDate ?: return null
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.YEAR)
    }

val TrackDto.coverArtworkUrl: String
    get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
