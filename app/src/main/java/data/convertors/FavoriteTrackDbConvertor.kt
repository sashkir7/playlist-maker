package data.convertors

import data.db.entity.FavoriteTrackEntity
import domain.player.Track
import java.util.Date

class FavoriteTrackDbConvertor {

    fun map(track: Track): FavoriteTrackEntity = FavoriteTrackEntity(
        trackId = track.trackId,
        trackName = track.trackName,
        artistName = track.artistName,
        collectionName = track.collectionName,
        releaseDate = track.releaseDate?.time,
        genreName = track.genreName,
        country = track.country,
        trackTimeMillis = track.trackTimeMillis,
        artworkUrl100 = track.artworkUrl100,
        previewUrl = track.previewUrl,
        timestamp = System.currentTimeMillis()
    )

    fun map(entity: FavoriteTrackEntity): Track = Track(
        trackId = entity.trackId,
        trackName = entity.trackName,
        artistName = entity.artistName,
        collectionName = entity.collectionName,
        releaseDate = if (entity.releaseDate == null) null else Date(entity.releaseDate),
        genreName = entity.genreName,
        country = entity.country,
        trackTimeMillis = entity.trackTimeMillis,
        artworkUrl100 = entity.artworkUrl100,
        previewUrl = entity.previewUrl
    )
}
