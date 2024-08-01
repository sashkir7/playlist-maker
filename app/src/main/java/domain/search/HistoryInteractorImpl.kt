package domain.search

import data.search.HistoryRepository
import data.search.network.TrackDto
import domain.player.Track

class HistoryInteractorImpl(
    private val repository: HistoryRepository
) : HistoryInteractor {

    override fun getHistory(): List<Track> =
        repository.getHistory().map { it.toModel() }

    override fun addTrack(track: Track) =
        repository.addTrack(TrackDto.fromModel(track))

    override fun clearAll() = repository.clearAll()
}
