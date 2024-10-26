package ui.media.playlists.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ui.media.playlists.details.PlaylistDetailsState.ReceivedPlaylist
import ui.media.playlists.details.PlaylistDetailsState.ReceivedTracks
import utils.EndingConvertor

class PlaylistDetailsFragment : Fragment() {

    companion object {
        private const val EXTRA_PLAYLIST_ID = "EXTRA_PLAYLIST_ID_KEY"

        fun createArgs(
            playlistId: Int
        ): Bundle = bundleOf(EXTRA_PLAYLIST_ID to playlistId)
    }

    private var _binding: FragmentPlaylistDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistDetailsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = requireArguments().getInt(EXTRA_PLAYLIST_ID)

        configureBackButton()

        viewModel.state.observe(viewLifecycleOwner) { render(it) }

        viewModel.getPlaylist(playlistId)
        viewModel.getTracksInPlaylist(playlistId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: PlaylistDetailsState) {
        when (state) {
            is ReceivedPlaylist -> {
                binding.tvPlaylistTitle.text = state.playlist.name
                binding.tvPlaylistDescription.text = state.playlist.description
                state.playlist.pathToImage?.let { setPlaylistCover(it) }
            }

            is ReceivedTracks -> {
                var totalMillis = 0L
                state.tracks.map { it.trackTimeMillis }.forEach { totalMillis += it }
                val totalMinutes = totalMillis / (60 * 1_000)
                binding.tvPlaylistTracksTime.text = EndingConvertor.minute(totalMinutes)
            }
        }
    }

    private fun configureBackButton() = binding.ivBack.setOnClickListener {
        findNavController().popBackStack()
    }

    private fun setPlaylistCover(
        imagePath: String
    ) = Glide.with(requireContext())
        .load(imagePath)
        .placeholder(R.drawable.track_placeholder)
        .into(binding.ivPlaylistCover)
}
