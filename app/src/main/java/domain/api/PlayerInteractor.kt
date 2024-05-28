package domain.api

interface PlayerInteractor {

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
