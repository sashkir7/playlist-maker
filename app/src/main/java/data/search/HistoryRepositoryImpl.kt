package data.search

import android.content.SharedPreferences
import data.search.network.TrackDto
import com.google.gson.Gson

class HistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : HistoryRepository {

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

    private fun deserialization(json: String): List<TrackDto> =
        gson.fromJson(json, Array<TrackDto>::class.java).toList()

    private fun serialization(tracks: List<TrackDto>): String = gson.toJson(tracks)
}
