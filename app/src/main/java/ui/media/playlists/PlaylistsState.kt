package ui.media.playlists

import domain.media.Playlist

sealed interface PlaylistsState {

    object Empty : PlaylistsState

    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistsState

}
