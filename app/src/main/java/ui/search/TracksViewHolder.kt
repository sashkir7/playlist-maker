package ui.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import domain.player.Track

class TracksViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val trackNameView: TextView = itemView.findViewById(R.id.trackName)
    private val artistNameView: TextView = itemView.findViewById(R.id.trackArtist)
    private val trackTimeView: TextView = itemView.findViewById(R.id.trackTime)
    private val trackImageView: ImageView = itemView.findViewById(R.id.trackImage)

    fun bind(model: Track) {
        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        trackTimeView.text = model.trackTimeAsString
        loadAndSetTrackImage(model.artworkUrl100)
    }

    private fun loadAndSetTrackImage(url: String) {
        Glide.with(itemView)
            .load(url)
            .placeholder(R.drawable.track_placeholder)
            .into(trackImageView)
    }
}