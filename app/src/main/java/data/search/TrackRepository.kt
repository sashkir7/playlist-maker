package data.search

import data.common.network.Resource
import data.search.network.TrackDto

interface TrackRepository {

    fun searchTrack(
        expression: String
    ): Resource<List<TrackDto>>
}