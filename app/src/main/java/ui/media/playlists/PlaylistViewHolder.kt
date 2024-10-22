package ui.media.playlists

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistViewBinding
import domain.media.Playlist

class PlaylistViewHolder(
    private val binding: PlaylistViewBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) = with(binding) {
        if (playlist.pathToImage == null) {
            ivPlaylistCover.setImageResource(R.drawable.track_placeholder)
        } else {
            Glide.with(itemView)
                .load(playlist.pathToImage)
                .centerCrop()
                .into(ivPlaylistCover)
        }

        tvPlaylistName.text = playlist.name
        tvPlaylistAmountTracks.text = getAmountTracksTitle(playlist.tracks.size)
    }

    private fun getAmountTracksTitle(amount: Int): String {
        val value = when {
            amount % 10 == 1 -> "трек"
            amount % 10 in 2..4 -> "трека"
            amount % 100 in 11..19 -> "треков"
            else -> "треков"
        }
        return "$amount $value"
    }
}