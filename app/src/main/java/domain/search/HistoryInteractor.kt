package domain.search

import domain.player.Track

interface HistoryInteractor {

    fun getHistory(): List<Track>

    fun addTrack(track: Track)

    fun clearAll()
}
