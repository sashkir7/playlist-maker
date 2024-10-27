package ui.media.playlists.details

import domain.media.Playlist
import domain.player.Track

sealed interface PlaylistDetailsState {

    data class ReceivedPlaylist(
        val playlist: Playlist
    ) : PlaylistDetailsState

    data class ReceivedTracks(
        val tracks: List<Track>
    ) : PlaylistDetailsState

    object SharedEmptyPlaylist : PlaylistDetailsState

    object DeletedPlaylist : PlaylistDetailsState

}
