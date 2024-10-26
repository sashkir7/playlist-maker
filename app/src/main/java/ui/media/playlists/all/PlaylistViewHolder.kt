package ui.media.playlists.all

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistViewBinding
import domain.media.Playlist
import utils.EndingConvertor

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
        tvPlaylistAmountTracks.text = EndingConvertor.track(playlist.tracks.size.toLong())
    }
}