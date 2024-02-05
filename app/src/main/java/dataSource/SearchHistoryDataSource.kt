package dataSource

import android.content.SharedPreferences
import api.dtos.TrackDto
import com.google.gson.Gson

const val SEARCH_HISTORY_SHARED_PREFERENCES = "HISTORY_TRACKS"

class SearchHistoryDataSource(
    private val sharedPrefs: SharedPreferences
) {

    companion object {
        private const val HISTORY_KEY = "HISTORY"
    }

    private val gson = Gson()

    fun getHistory(): List<TrackDto> {
        val json = sharedPrefs
            .getString(HISTORY_KEY, null)
            ?: return emptyList()
        return deserialization(json)
    }

    fun addTrack(track: TrackDto) {
        val tracks = getHistory().toMutableList()

        tracks.remove(track)
        tracks.add(0, track)
        while (tracks.size > 10) { tracks.removeLast() }

        sharedPrefs.edit()
            .putString(HISTORY_KEY, serialization(tracks))
            .apply()
    }

    fun clearAll(): Unit = sharedPrefs.edit().clear().apply()

    private fun deserialization(json: String): List<TrackDto> =
        gson.fromJson(json, Array<TrackDto>::class.java).toList()

    private fun serialization(tracks: List<TrackDto>): String = gson.toJson(tracks)
}
