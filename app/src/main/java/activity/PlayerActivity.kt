package activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import api.dtos.TrackDto
import api.dtos.coverArtworkUrl
import api.dtos.releaseDateYear
import api.dtos.trackTimeAsString
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import utils.cornerRadius
import utils.isVisible

class PlayerActivity : AppCompatActivity() {

    private val backButton by lazy { findViewById<ImageView>(R.id.iv_back) }
    private val coverImageView by lazy { findViewById<ImageView>(R.id.iv_trackCover) }

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

        val track = checkNotNull(intent.extras)
            .getSerializable(TrackDto::class.java.simpleName) as TrackDto

        configureBackButton()
        configureTrackCover(track.coverArtworkUrl)
        configureTrackInformation(track)
    }

    private fun configureBackButton() = backButton.setOnClickListener { onBackPressed() }

    private fun configureTrackCover(url: String) {
        Glide.with(applicationContext)
            .load(url)
            .cornerRadius(value = 8)
            .placeholder(R.drawable.track_placeholder)
            .into(coverImageView)
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
        groupView.visibility = if (value == null) GONE else VISIBLE
    }
}
