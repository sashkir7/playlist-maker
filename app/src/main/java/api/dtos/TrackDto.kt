package api.dtos

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class TrackDto(
    @SerializedName("trackName") val trackName: String,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("trackTimeMillis") val trackTimeMillis: Long,
    @SerializedName("artworkUrl100") val artworkUrl100: String
)

fun TrackDto.trackTimeAsString(): String =
    SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
