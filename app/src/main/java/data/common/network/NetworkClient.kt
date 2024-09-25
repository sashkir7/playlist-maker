package data.common.network

interface NetworkClient {

    suspend fun doRequest(dto: Any): Response
}