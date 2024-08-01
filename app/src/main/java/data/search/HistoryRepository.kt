package data.search

import data.search.network.TrackDto

interface HistoryRepository {

    fun getHistory(): List<TrackDto>

    fun addTrack(track: TrackDto)

    fun clearAll()
}
