package ui.media.playlists.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import domain.media.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel
import ui.media.playlists.all.PlaylistsState.Content
import ui.media.playlists.all.PlaylistsState.Empty
import ui.media.playlists.details.PlaylistDetailsFragment
import utils.isVisible

class PlaylistsFragment : Fragment() {

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistsViewModel by viewModel()

    private val playlistsAdapter by lazy {
        PlaylistAdapter { playlist -> navigateToPlaylistDetails(playlist) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurePlaylistRecycler()
        configureNewPlaylistButton()

        viewModel.state.observe(viewLifecycleOwner) { render(it) }
        viewModel.fillData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: PlaylistsState) {
        val recycler = binding.playlistsRecycler
        val emptyPlaylistView = binding.emptyPlaylistsView

        when (state) {
            is Empty -> {
                recycler.isVisible = false
                emptyPlaylistView.isVisible = true
            }

            is Content -> {
                recycler.isVisible = true
                emptyPlaylistView.isVisible = false
                playlistsAdapter.setPlaylists(state.playlists)
            }
        }
    }

    private fun configurePlaylistRecycler() = with(binding.playlistsRecycler) {
        adapter = playlistsAdapter
        layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun configureNewPlaylistButton() = binding.newPlaylistButton
        .setOnClickListener { navigateToNewPlaylist() }

    private fun navigateToNewPlaylist() =
        findNavController().navigate(R.id.action_mediaFragment_to_newPlaylistFragment)

    private fun navigateToPlaylistDetails(
        playlist: Playlist
    ) = findNavController().navigate(
        resId = R.id.action_mediaFragment_to_playlistDetailsFragment,
        args = PlaylistDetailsFragment.createArgs(checkNotNull(playlist.id))
    )
}
