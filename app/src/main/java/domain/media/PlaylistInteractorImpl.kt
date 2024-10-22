package domain.media

import data.media.playlists.new.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val repository: PlaylistRepository
) : PlaylistInteractor {

    override fun getAll(): Flow<List<Playlist>> = repository.getAll()

}
