package ui.media.favoriteTracks

import domain.player.Track

sealed class FavoriteTracksState {

    object Empty : FavoriteTracksState()

    data class Content(
        val tracks: List<Track>
    ) : FavoriteTracksState()

}
