package domain.player

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val collectionName: String?,
    val releaseDate: Date?,
    val genreName: String?,
    val country: String?,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val previewUrl: String
) : Serializable {

    val releaseDateYear: Int?
        get() = with(Calendar.getInstance()) {
            time = releaseDate ?: return null
            return get(Calendar.YEAR)
        }

    val trackTimeAsString: String
        get() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)

    val coverArtworkUrl: String
        get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}
