package ui.media.favoriteTracks

import domain.player.Track

sealed interface FavoriteTracksState {

    object Empty : FavoriteTracksState

    data class Content(
        val tracks: List<Track>
    ) : FavoriteTracksState

}
