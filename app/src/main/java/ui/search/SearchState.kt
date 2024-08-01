package ui.search

import domain.player.Track

sealed class SearchState {

    object Loading : SearchState()

    class SearchHistory(
        val tracks: List<Track>
    ) : SearchState()

    class SearchedTracks(
        val tracks: List<Track>
    ) : SearchState()

    class SearchError(
        val error: String
    ) : SearchState()
}