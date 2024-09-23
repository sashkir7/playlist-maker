package domain.search

import domain.player.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {

    fun getHistory(): List<Track>

    fun addTrack(track: Track)

    fun clearAll()

    fun searchTrack(
        expression: String
    ): Flow<Pair<List<Track>?, String?>>
}
