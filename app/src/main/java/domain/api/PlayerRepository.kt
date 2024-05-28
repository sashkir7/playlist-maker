package domain.api

interface PlayerRepository {

    fun prepare(
        previewUrl: String,
        onPrepareListener: () -> Unit,
        onCompletionListener: () -> Unit
    )

    fun start()
    fun pause()
    fun currentPosition(): String
    fun release()

}