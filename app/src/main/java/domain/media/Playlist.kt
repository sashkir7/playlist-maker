package domain.media

import java.io.Serializable

data class Playlist(
    val id: Int? = null,
    val name: String,
    val description: String?,
    val pathToImage: String?,
    val tracks: List<Int> = emptyList()
) : Serializable
