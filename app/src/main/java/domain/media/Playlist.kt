package domain.media

import domain.player.Track
import java.io.Serializable

data class Playlist(
    val id: Int? = null,
    var name: String,
    var description: String?,
    var pathToImage: String?,
    val tracks: MutableList<Long> = mutableListOf()
) : Serializable {

    fun addTrack(track: Track) = tracks.add(track.trackId)

    fun containsTrack(
        track: Track
    ): Boolean = tracks.contains(track.trackId)
}
