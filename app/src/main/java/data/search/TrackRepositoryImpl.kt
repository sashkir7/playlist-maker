package data.search

import data.common.network.NetworkClient
import data.common.network.Resource
import data.search.network.TrackDto
import data.search.network.TrackSearchRequestDto
import data.search.network.TracksResponseDto

class TrackRepositoryImpl(
    private val networkClient: NetworkClient
) : TrackRepository {

    override fun searchTrack(
        expression: String
    ): Resource<List<TrackDto>> {
        val response = networkClient.doRequest(TrackSearchRequestDto(expression))
        return when (response.resultCode) {
            -1 -> Resource.Error(message = "No Internet connection")
            200 -> Resource.Success(data = (response as TracksResponseDto).results)
            else -> Resource.Error(message = "Something went wrong")
        }
    }
}
