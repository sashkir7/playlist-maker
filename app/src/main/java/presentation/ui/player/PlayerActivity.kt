package presentation.ui.player

import Creator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import api.dtos.TrackDto
import api.dtos.coverArtworkUrl
import api.dtos.releaseDateYear
import api.dtos.trackTimeAsString
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import enums.MediaPlayerState.DEFAULT
import enums.MediaPlayerState.PAUSED
import enums.MediaPlayerState.PLAYING
import enums.MediaPlayerState.PREPARED
import utils.cornerRadius
import utils.isVisible

class PlayerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TRACK_KEY = "EXTRA_TRACK_KEY"
        private const val TRACK_CURRENT_POSITION_DELAY = 250L
    }

    private val interactor = Creator.providePlayerInteractor()

    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private val updateDurationValueRunnable = object : Runnable {
        override fun run() {
            trackCurrentPosition.text = interactor.currentPosition()
            mainThreadHandler.postDelayed(this, TRACK_CURRENT_POSITION_DELAY)
        }
    }

    private val backButton by lazy { findViewById<ImageView>(R.id.iv_back) }
    private val coverImageView by lazy { findViewById<ImageView>(R.id.iv_trackCover) }

    private val playOrPauseButton by lazy { findViewById<ImageView>(R.id.iv_playButton) }
    private val trackCurrentPosition by lazy { findViewById<TextView>(R.id.tv_trackCurrentPosition) }

    private val trackName by lazy { findViewById<TextView>(R.id.tv_trackName) }
    private val artistName by lazy { findViewById<TextView>(R.id.tv_artistName) }

    private val duration by lazy { findViewById<TextView>(R.id.tv_durationValue) }

    private val collectionName by lazy { findViewById<TextView>(R.id.tv_collectionNameValue) }
    private val collectionNameGroup by lazy { findViewById<View>(R.id.gr_collectionName) }

    private val releaseDate by lazy { findViewById<TextView>(R.id.tv_releaseDateValue) }
    private val releaseDateGroup by lazy { findViewById<View>(R.id.gr_releaseDate) }

    private val genreName by lazy { findViewById<TextView>(R.id.tv_genreNameValue) }
    private val genreNameGroup by lazy { findViewById<View>(R.id.gr_genreName) }

    private val country by lazy { findViewById<TextView>(R.id.tv_countryValue) }
    private val countryGroup by lazy { findViewById<View>(R.id.gr_country) }

    private var playerState = DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        // Здесь должна быть domain-модель вместо dto
        // Изменения затрагивают не только экран плеера, поэтому оставил комментарий
        val track = checkNotNull(intent.extras)
            .getSerializable(EXTRA_TRACK_KEY) as TrackDto

        configureBackButton()
        configureTrackCover(track.coverArtworkUrl)
        configureTrackInformation(track)

        configurePlayButton()
        configureMediaPlayer(track.previewUrl)
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        interactor.release()
    }

    private fun configureBackButton() = backButton.setOnClickListener { onBackPressed() }

    private fun configureTrackCover(url: String) {
        Glide.with(applicationContext)
            .load(url)
            .cornerRadius(value = 8)
            .placeholder(R.drawable.track_placeholder)
            .into(coverImageView)
    }

    private fun configurePlayButton() {
        playOrPauseButton.setOnClickListener {
            when (playerState) {
                PLAYING -> pausePlayer()
                PREPARED, PAUSED -> startPlayer()
                else -> showPlayerIsNotPreparedToast()
            }
        }
    }

    private fun configureTrackInformation(track: TrackDto) {
        trackName.text = track.trackName
        artistName.text = track.artistName

        duration.text = track.trackTimeAsString
        configureTextView(collectionName, collectionNameGroup, track.collectionName)
        configureTextView(releaseDate, releaseDateGroup, track.releaseDateYear?.toString())
        configureTextView(genreName, genreNameGroup, track.genreName)
        configureTextView(country, countryGroup, track.country)
    }

    private fun configureTextView(textView: TextView, groupView: View, value: String?) {
        textView.text = value
        groupView.isVisible = value != null
    }

    private fun configureMediaPlayer(previewUrl: String) {
        interactor.prepare(
            previewUrl = previewUrl,
            onPrepareListener = { playerState = PREPARED },
            onCompletionListener = {
                playerState = PREPARED
                mainThreadHandler.removeCallbacks(updateDurationValueRunnable)
                trackCurrentPosition.text = getText(R.string.track_duration_zero)
                playOrPauseButton.setImageResource(R.drawable.play_icon)
            }
        )
    }

    private fun pausePlayer() {
        interactor.pause()
        playOrPauseButton.setImageResource(R.drawable.play_icon)
        playerState = PAUSED
        mainThreadHandler.removeCallbacks(updateDurationValueRunnable)
    }

    private fun startPlayer() {
        interactor.start()
        playOrPauseButton.setImageResource(R.drawable.pause_icon)
        playerState = PLAYING
        mainThreadHandler.post(updateDurationValueRunnable)
    }

    private fun showPlayerIsNotPreparedToast() {
        val text = getText(R.string.media_player_is_not_prepared)
        Toast.makeText(this, text, LENGTH_SHORT).show()
    }
}
