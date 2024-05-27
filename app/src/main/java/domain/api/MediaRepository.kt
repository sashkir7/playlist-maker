package domain.api

import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener

interface MediaRepository {

    fun prepare(
        previewUrl: String,
        onPrepareListener: OnPreparedListener,
        onCompletionListener: OnCompletionListener
    )

    fun start()

    fun pause()

    fun currentPosition(): Int

    fun release()

}