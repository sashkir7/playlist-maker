package api.dtos

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class TrackDto(
    @SerializedName("trackId") val trackId: Int,
    @SerializedName("trackName") val trackName: String,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("trackTimeMillis") val trackTimeMillis: Long,
    @SerializedName("artworkUrl100") val artworkUrl100: String
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return trackId == (other as TrackDto).trackId
    }

    override fun hashCode(): Int = trackId
}

fun TrackDto.trackTimeAsString(): String =
    SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
