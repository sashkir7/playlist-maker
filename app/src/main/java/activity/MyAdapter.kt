package activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import model.Track

class TracksViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackNameTextView: TextView

    init {
        trackNameTextView = itemView.findViewById(R.id.trackName)
    }

    fun bind(model: Track) {
        trackNameTextView.text = model.trackName
    }
}

class MyAdapter(val tracks: List<Track>): RecyclerView.Adapter<TracksViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_track, parent, false)
        return TracksViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
    }
}