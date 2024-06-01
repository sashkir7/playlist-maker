package data

import android.media.MediaPlayer
import domain.api.PlayerRepository
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerRepositoryImpl : PlayerRepository {

    private val player = MediaPlayer()
    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

    override fun prepare(
        previewUrl: String,
        onPrepareListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) = with(player) {
        setDataSource(previewUrl)
        prepareAsync()
        setOnPreparedListener { onPrepareListener() }
        setOnCompletionListener { onCompletionListener() }
    }

    override fun start() = player.start()

    override fun pause() = player.pause()

    override fun currentPosition(): String =
        dateFormat.format(player.currentPosition)

    override fun release() = player.release()

}
