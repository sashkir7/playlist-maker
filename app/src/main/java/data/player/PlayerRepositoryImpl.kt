package data.player

import android.media.MediaPlayer
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerRepositoryImpl : PlayerRepository {

    private var player = MediaPlayer()
    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

    override fun prepare(
        previewUrl: String,
        onPrepareListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) {
        player = MediaPlayer()
        with(player) {
            setDataSource(previewUrl)
            prepareAsync()
            setOnPreparedListener { onPrepareListener() }
            setOnCompletionListener { onCompletionListener() }
        }
    }

    override fun start() = player.start()

    override fun pause() = player.pause()

    override fun currentPosition(): String =
        dateFormat.format(player.currentPosition)

    override fun release() = player.release()
}
