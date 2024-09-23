package data.player

interface PlayerRepository {

    fun prepare(
        previewUrl: String,
        onPrepareListener: () -> Unit,
        onCompletionListener: () -> Unit
    )

    fun start()
    fun pause()
    fun currentPosition(): String
    fun isPlaying(): Boolean
    fun release()

}