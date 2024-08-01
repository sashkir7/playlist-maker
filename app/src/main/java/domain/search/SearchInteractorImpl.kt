package domain.search

import data.common.network.Resource
import data.search.SearchRepository
import data.search.network.TrackDto
import domain.player.Track
import java.util.concurrent.Executors

class SearchInteractorImpl(
    private val repository: SearchRepository
) : SearchInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun getHistory(): List<Track> =
        repository.getHistory().map { it.toModel() }

    override fun addTrack(track: Track) =
        repository.addTrack(TrackDto.fromModel(track))

    override fun clearAll() = repository.clearAll()

    override fun searchTrack(
        expression: String,
        consumer: SearchInteractor.Consumer
    ) = executor.execute {
        when (val resource = repository.searchTrack(expression)) {
            is Resource.Success -> consumer.consume(
                foundTracks = resource.data?.map { it.toModel() },
                errorMessage = null
            )

            is Resource.Error -> consumer.consume(
                foundTracks = null,
                errorMessage = resource.message
            )
        }
    }
}
