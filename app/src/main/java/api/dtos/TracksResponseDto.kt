package api.dtos

import com.google.gson.annotations.SerializedName

data class TracksResponseDto(
    @SerializedName("resultCount") val resultCount: Int,
    @SerializedName("results") val results: List<TrackDto>
)
