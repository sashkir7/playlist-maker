package data

import android.media.MediaPlayer
import domain.api.MediaRepository

class MediaRepositoryImpl : MediaRepository {

    private val player = MediaPlayer()

    override fun prepare(
        previewUrl: String,
        onPrepareListener: MediaPlayer.OnPreparedListener,
        onCompletionListener: MediaPlayer.OnCompletionListener
    ) = with(player) {
        setDataSource(previewUrl)
        prepareAsync()
        setOnPreparedListener(onPrepareListener)
        setOnCompletionListener(onCompletionListener)
    }

    override fun start() = player.start()

    override fun pause() = player.pause()

    override fun currentPosition(): Int = player.currentPosition

    override fun release() = player.release()

}
