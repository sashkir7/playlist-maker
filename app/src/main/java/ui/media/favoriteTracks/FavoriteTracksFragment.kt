package ui.media.favoriteTracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import domain.player.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import ui.media.favoriteTracks.FavoriteTracksState.Content
import ui.media.favoriteTracks.FavoriteTracksState.Empty
import ui.player.PlayerFragment
import ui.search.TracksAdapter
import utils.isVisible

class FavoriteTracksFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteTracksViewModel by viewModel()

    private val tracksAdapter by lazy {
        TracksAdapter { track -> navigateToPlayer(track) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureTracksRecycler()

        viewModel.state.observe(viewLifecycleOwner) { render(it) }
        viewModel.fillData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: FavoriteTracksState) {
        val recycler = binding.favoriteTracksRecycler
        val emptyTracksView = binding.emptyFavoriteTracksView
        when (state) {
            is Empty -> {
                recycler.isVisible = false
                emptyTracksView.isVisible = true
            }

            is Content -> {
                emptyTracksView.isVisible = false
                tracksAdapter.setTracks(state.tracks)
                recycler.isVisible = true
            }
        }
    }

    private fun configureTracksRecycler() = with(binding.favoriteTracksRecycler) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = tracksAdapter
    }

    private fun navigateToPlayer(track: Track) = findNavController().navigate(
        resId = R.id.action_mediaFragment_to_playerFragment,
        args = PlayerFragment.createArgs(track)
    )
}
