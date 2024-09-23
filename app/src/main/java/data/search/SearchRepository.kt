package data.search

import data.common.network.Resource
import data.search.network.TrackDto
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun getHistory(): List<TrackDto>

    fun addTrack(track: TrackDto)

    fun clearAll()

    fun searchTrack(
        expression: String
    ): Flow<Resource<List<TrackDto>>>
}
