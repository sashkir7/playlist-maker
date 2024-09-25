package data.search.network

import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesService {

    @GET("/search?entity=song")
    suspend fun search(
        @Query("term") text: String
    ): TracksResponseDto
}
