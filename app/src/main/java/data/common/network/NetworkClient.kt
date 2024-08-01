package data.common.network

interface NetworkClient {

    fun doRequest(dto: Any): Response
}