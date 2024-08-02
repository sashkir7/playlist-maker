package domain.search

import domain.player.Track

interface SearchInteractor {

    fun getHistory(): List<Track>

    fun addTrack(track: Track)

    fun clearAll()

    fun searchTrack(expression: String, consumer: Consumer)

    interface Consumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }
}
