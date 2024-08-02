package data.search.network

import com.google.gson.annotations.SerializedName
import data.common.network.Response

data class TracksResponseDto(
    @SerializedName("resultCount") val resultCount: Int,
    @SerializedName("results") val results: List<TrackDto>
) : Response()
