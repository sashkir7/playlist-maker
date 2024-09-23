package domain.player

import data.player.PlayerRepository

class PlayerInteractorImpl(
    private val repository: PlayerRepository
) : PlayerInteractor {

    override fun prepare(
        previewUrl: String,
        onPrepareListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) = repository.prepare(
        previewUrl, onPrepareListener, onCompletionListener
    )

    override fun start() = repository.start()

    override fun pause() = repository.pause()

    override fun currentPosition() = repository.currentPosition()

    override fun isPlaying() = repository.isPlaying()

    override fun release() = repository.release()
}
