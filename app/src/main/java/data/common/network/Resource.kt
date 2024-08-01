package data.common.network

sealed class Resource<T>(
    val data: T?,
    val message: String?
) {

    class Success<T>(
        data: T
    ) : Resource<T>(data = data, message = null)

    class Error<T>(
        message: String,
        data: T? = null
    ) : Resource<T>(data = data, message = message)
}
