package domain.search

import data.common.network.Resource
import data.search.SearchRepository
import data.search.network.TrackDto
import domain.player.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(
    private val repository: SearchRepository
) : SearchInteractor {

    override fun getHistory(): List<Track> =
        repository.getHistory().map { it.toModel() }

    override fun addTrack(track: Track) =
        repository.addTrack(TrackDto.fromModel(track))

    override fun clearAll() = repository.clearAll()

    override fun searchTrack(expression: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTrack(expression).map { resource ->
            when (resource) {
                is Resource.Success -> Pair(resource.data?.map { it.toModel() }, null)
                is Resource.Error -> Pair(null, resource.message)
            }
        }
    }
}
