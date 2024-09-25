package data.search

import android.content.SharedPreferences
import data.search.network.TrackDto
import com.google.gson.Gson
import data.common.network.NetworkClient
import data.common.network.Resource
import data.search.network.TrackSearchRequestDto
import data.search.network.TracksResponseDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val sharedPreferences: SharedPreferences
) : SearchRepository {

    companion object {
        private const val HISTORY_KEY = "HISTORY"
    }

    private val gson = Gson()

    override fun getHistory(): List<TrackDto> {
        val json = sharedPreferences
            .getString(HISTORY_KEY, null)
            ?: return emptyList()
        return deserialization(json)
    }

    override fun addTrack(track: TrackDto) {
        val tracks = getHistory().toMutableList()

        tracks.remove(track)
        tracks.add(0, track)
        while (tracks.size > 10) { tracks.removeLast() }

        sharedPreferences.edit()
            .putString(HISTORY_KEY, serialization(tracks))
            .apply()
    }

    override fun clearAll(): Unit = sharedPreferences.edit().clear().apply()

    override fun searchTrack(
        expression: String
    ): Flow<Resource<List<TrackDto>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequestDto(expression))
        when (response.resultCode) {
            -1 -> emit(Resource.Error(message = "No Internet connection"))
            200 -> emit(Resource.Success(data = (response as TracksResponseDto).results))
            else -> emit(Resource.Error(message = "Something went wrong"))
        }
    }

    private fun deserialization(json: String): List<TrackDto> =
        gson.fromJson(json, Array<TrackDto>::class.java).toList()

    private fun serialization(tracks: List<TrackDto>): String = gson.toJson(tracks)
}
