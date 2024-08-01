package ui.player

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import domain.models.Track
import domain.player.MediaPlayerState.DEFAULT
import domain.player.MediaPlayerState.PAUSED
import domain.player.MediaPlayerState.PLAYING
import domain.player.MediaPlayerState.PREPARED
import utils.cornerRadius
import utils.isVisible

class PlayerActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory()
        )[PlayerViewModel::class.java]
    }

    companion object {
        const val EXTRA_TRACK_KEY = "EXTRA_TRACK_KEY"
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        viewModel.currentPosition.observe(this) {
            trackCurrentPosition.text = it
        }

        viewModel.playerState.observe(this) {
            val image = when (checkNotNull(it)) {
                PLAYING -> R.drawable.pause_icon
                DEFAULT, PREPARED, PAUSED -> R.drawable.play_icon
            }
            playOrPauseButton.setImageResource(image)
        }

        val track = checkNotNull(intent.extras)
            .getSerializable(EXTRA_TRACK_KEY) as Track

        configureBackButton()
        configureTrackCover(track.coverArtworkUrl)
        configureTrackInformation(track)

        configurePlayButton()
        configureMediaPlayer(track.previewUrl)
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
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
            when (viewModel.playerState.value) {
                PLAYING -> viewModel.pause()
                PREPARED, PAUSED -> viewModel.start()
                else -> showPlayerIsNotPreparedToast()
            }
        }
    }

    private fun configureTrackInformation(track: Track) {
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

    private fun configureMediaPlayer(previewUrl: String) = viewModel.prepare(previewUrl)

    private fun showPlayerIsNotPreparedToast() {
        val text = getText(R.string.media_player_is_not_prepared)
        Toast.makeText(this, text, LENGTH_SHORT).show()
    }
}
