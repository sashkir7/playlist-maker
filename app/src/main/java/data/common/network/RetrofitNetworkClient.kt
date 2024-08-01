package data.common.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import data.search.network.ITunesService
import data.search.network.TrackSearchRequestDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(
    private val context: Context
) : NetworkClient {

    companion object {
        private const val ITUNES_BASE_URL = "https://itunes.apple.com"
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ITunesService::class.java)

    override fun doRequest(dto: Any): Response {
        if (!isConnected()) return Response(resultCode = -1)
        if (dto !is TrackSearchRequestDto) return Response(resultCode = 400)

        val response = itunesService.search(dto.expression).execute()
        return (response.body() ?: Response())
            .apply { resultCode = response.code() }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(
            connectivityManager.activeNetwork
        ) ?: return false

        when {
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> return true
            capabilities.hasTransport(TRANSPORT_WIFI) -> return true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> return true
        }

        return false
    }
}