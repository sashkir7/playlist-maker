package ui.player

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlayerPlaylistViewBinding
import domain.media.Playlist
import utils.EndingConvertor

class PlayerPlaylistViewHolder(
    private val binding: PlayerPlaylistViewBinding
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
        tvPlaylistAmountTracks.text = EndingConvertor.track(playlist.tracks.size)
    }

}
