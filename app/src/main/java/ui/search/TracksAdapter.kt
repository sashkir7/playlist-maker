package ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import domain.player.Track

class TracksAdapter(
    private val onLongClickAction: (track: Track) -> Unit = {},
    private val onClickAction: (track: Track) -> Unit,
) : RecyclerView.Adapter<TracksViewHolder>() {

    private var tracks: List<Track> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_track, parent, false)
        return TracksViewHolder(view)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener { onClickAction(track) }
        holder.itemView.setOnLongClickListener { onLongClickAction(track); true }
    }

    fun setTracks(tracks: List<Track>) {
        this.tracks = tracks
        notifyDataSetChanged()
    }

    fun clearAll(): Unit = setTracks(emptyList())
}
