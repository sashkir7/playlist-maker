package viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import model.Track

class TracksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackNameView: TextView
    private val artistNameView: TextView
    private val trackTimeView: TextView
    private val trackImageView: ImageView

    init {
        trackNameView = itemView.findViewById(R.id.trackName)
        artistNameView = itemView.findViewById(R.id.trackArtist)
        trackTimeView = itemView.findViewById(R.id.trackTime)
        trackImageView = itemView.findViewById(R.id.trackImage)
    }

    fun bind(model: Track) {
        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        trackTimeView.text = model.trackTime
        loadAndSetTrackImage(model.artworkUrl100)
    }

    private fun loadAndSetTrackImage(url: String) {
        Glide.with(itemView)
            .load(url)
            .placeholder(R.drawable.track_placeholder)
            .into(trackImageView)
    }
}