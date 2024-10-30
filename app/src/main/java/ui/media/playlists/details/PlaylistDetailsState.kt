package ui.media.playlists.details

import domain.media.Playlist

sealed interface PlaylistDetailsState {

    data class ReceivedPlaylist(
        val playlist: Playlist
    ) : PlaylistDetailsState

    object SharedEmptyPlaylist : PlaylistDetailsState

    object DeletedPlaylist : PlaylistDetailsState

}
