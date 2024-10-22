package domain.media

import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    fun getAll(): Flow<List<Playlist>>

}
