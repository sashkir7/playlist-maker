package ui.media.playlists.all

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistViewBinding
import domain.media.Playlist

class PlaylistAdapter(
    private val onClickAction: (playlist: Playlist) -> Unit
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    private var playlists = listOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = PlaylistViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PlaylistViewHolder(binding)
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener { onClickAction(playlist) }
    }

    fun setPlaylists(playlists: List<Playlist>) {
        this.playlists = playlists
        notifyDataSetChanged()
    }
}
