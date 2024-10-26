package data.search.network

import com.google.gson.annotations.SerializedName
import domain.player.Track
import java.io.Serializable
import java.util.Date

data class TrackDto(
    @SerializedName("trackId") val trackId: Long,
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

    companion object {
        fun fromModel(model: Track): TrackDto = TrackDto(
            trackId = model.trackId,
            trackName = model.trackName,
            artistName = model.artistName,
            collectionName = model.collectionName,
            releaseDate = model.releaseDate,
            genreName = model.genreName,
            country = model.country,
            trackTimeMillis = model.trackTimeMillis,
            artworkUrl100 = model.artworkUrl100,
            previewUrl = model.previewUrl
        )
    }

    fun toModel(): Track = Track(
        trackId = trackId,
        trackName = trackName,
        artistName = artistName,
        collectionName = collectionName,
        releaseDate = releaseDate,
        genreName = genreName,
        country = country,
        trackTimeMillis = trackTimeMillis,
        artworkUrl100 = artworkUrl100,
        previewUrl = previewUrl
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return trackId == (other as TrackDto).trackId
    }

    override fun hashCode(): Int = trackId.toInt()
}
