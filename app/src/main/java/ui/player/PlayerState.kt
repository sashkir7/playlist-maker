package ui.player

import domain.media.Playlist

sealed interface PlayerState {

    object Default : PlayerState

    object Prepared : PlayerState

    data class Playing(
        val currentPosition: String
    ) : PlayerState

    object Paused : PlayerState

    data class Favorite(
        val isFavorite: Boolean
    ) : PlayerState

    data class GetPlaylists(
        val playlists: List<Playlist>
    ) : PlayerState
}
