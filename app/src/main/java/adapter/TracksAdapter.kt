package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import model.Track
import viewHolder.TracksViewHolder

class TracksAdapter(private val tracks: List<Track>) : RecyclerView.Adapter<TracksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val trackView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_track, parent, false)
        return TracksViewHolder(trackView)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(
        holder: TracksViewHolder,
        position: Int
    ): Unit = holder.bind(tracks[position])
}
