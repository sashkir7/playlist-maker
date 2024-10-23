package ui.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlayerPlaylistViewBinding
import domain.media.Playlist

class PlayerPlaylistAdapter : RecyclerView.Adapter<PlayerPlaylistViewHolder>() {

    private var playlists = listOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerPlaylistViewHolder {
        val binding = PlayerPlaylistViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PlayerPlaylistViewHolder(binding)
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlayerPlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    fun setPlaylists(playlists: List<Playlist>) {
        this.playlists = playlists
        notifyDataSetChanged()
    }
}
