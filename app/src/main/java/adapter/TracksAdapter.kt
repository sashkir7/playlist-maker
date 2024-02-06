package adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import api.dtos.TrackDto
import com.example.playlistmaker.R
import viewHolder.TracksViewHolder

class TracksAdapter(
    private val onClickListener: (track: TrackDto) -> OnClickListener
) : RecyclerView.Adapter<TracksViewHolder>() {

    private var tracks: List<TrackDto> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_track, parent, false)
        return TracksViewHolder(view)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener(onClickListener(track))
    }

    fun setTracks(tracks: List<TrackDto>) {
        this.tracks = tracks
        notifyDataSetChanged()
    }

    fun clearAll(): Unit = setTracks(emptyList())
}
