package domain.search

import data.common.network.Resource
import data.search.TrackRepository
import java.util.concurrent.Executors

class TrackInteractorImpl(
    private val repository: TrackRepository
) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(
        expression: String,
        consumer: TrackInteractor.Consumer
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
