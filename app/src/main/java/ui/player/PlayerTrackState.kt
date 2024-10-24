package ui.player

import domain.media.Playlist

sealed interface PlayerTrackState {

    object InFavorite : PlayerTrackState

    object NotInFavorite : PlayerTrackState

    data class ReceivedPlaylists(
        val playlists: List<Playlist>
    ) : PlayerTrackState

    data class AddedToPlaylist(
        val playlist: Playlist
    ) : PlayerTrackState

    data class AlreadyInPlaylist(
        val playlist: Playlist
    ) : PlayerTrackState
}