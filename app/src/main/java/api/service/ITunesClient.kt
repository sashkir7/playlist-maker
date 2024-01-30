package api.service

import api.dtos.TracksResponseDto
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ITunesClient {

    private const val ITUNES_BASE_URL = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(ITunesService::class.java)

    fun search(
        text: String,
        callback: Callback<TracksResponseDto>
    ): Unit = service.search(text).enqueue(callback)

}