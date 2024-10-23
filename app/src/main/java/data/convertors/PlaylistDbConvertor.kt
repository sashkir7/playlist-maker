package data.convertors

import data.db.entity.PlaylistEntity
import domain.media.Playlist

class PlaylistDbConvertor {

    fun map(playlist: Playlist): PlaylistEntity = PlaylistEntity(
        id = playlist.id,
        name = playlist.name,
        description = playlist.description,
        pathToImage = playlist.pathToImage,
        trackIds = playlist.tracks,
        timestamp = System.currentTimeMillis()
    )

    fun map(entity: PlaylistEntity): Playlist = Playlist(
        id = entity.id,
        name = entity.name,
        description = entity.description,
        pathToImage = entity.pathToImage,
        tracks = entity.trackIds.toMutableList()
    )
}
