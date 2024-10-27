package ui.media.playlists.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import domain.player.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import ui.media.playlists.details.PlaylistDetailsState.DeletedPlaylist
import ui.media.playlists.details.PlaylistDetailsState.ReceivedPlaylist
import ui.media.playlists.details.PlaylistDetailsState.SharedEmptyPlaylist
import ui.media.playlists.modify.edit.EditPlaylistFragment
import ui.player.PlayerFragment
import ui.search.TracksAdapter
import utils.EndingConvertor
import utils.isVisible

class PlaylistDetailsFragment : Fragment() {

    companion object {
        private const val EXTRA_PLAYLIST_ID = "EXTRA_PLAYLIST_ID_KEY"

        fun createArgs(
            playlistId: Int
        ): Bundle = bundleOf(EXTRA_PLAYLIST_ID to playlistId)
    }

    private var playlistId: Int = -1

    private var _binding: FragmentPlaylistDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistDetailsViewModel by viewModel()

    private lateinit var menuBottomSheet: BottomSheetBehavior<LinearLayout>

    private val tracksAdapter by lazy {
        TracksAdapter(
            onClickAction = { track -> navigateToPlayer(track) },
            onLongClickAction = { track -> showConfirmDeleteTrackDialog(playlistId, track) }
        )
    }

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

        playlistId = requireArguments().getInt(EXTRA_PLAYLIST_ID)
        menuBottomSheet = BottomSheetBehavior.from(binding.menuBottomSheet)
        menuBottomSheet.state = STATE_HIDDEN

        configureBackButton()
        configureShareButton()
        configureMenuButton()
        configureTracksRecycler()
        configureMenuBottomSheet()

        viewModel.playlistState.observe(viewLifecycleOwner) { render(it) }
        viewModel.tracksState.observe(viewLifecycleOwner) { render(it) }

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

                binding.tvSharePlaylistName.text = state.playlist.name
                binding.tvSharePlaylistAmountTracks.text =
                    EndingConvertor.track(state.playlist.tracks.size)

                state.playlist.pathToImage?.let {
                    setPlaylistCover(it, binding.ivPlaylistCover)
                    setPlaylistCover(it, binding.ivSharePlaylistCover)
                }
            }

            SharedEmptyPlaylist -> showToast(getString(R.string.shared_empty_playlist_message))

            DeletedPlaylist -> findNavController().navigateUp()
        }
    }

    private fun render(tracks: List<Track>) {
        var totalMillis = 0L
        tracks.map { it.trackTimeMillis }.forEach { totalMillis += it }
        val minutesPart = EndingConvertor.minute(totalMillis / (60 * 1_000))
        val tracksPart = EndingConvertor.track(tracks.size)
        binding.tvPlaylistTracksTime.text = "$minutesPart • $tracksPart"

        tracksAdapter.setTracks(tracks)
        if (tracks.isEmpty()) {
            binding.rvPlaylistTracks.isVisible = false
            binding.tvPlaylistEmptyTracks.isVisible = true
        } else {
            binding.rvPlaylistTracks.isVisible = true
            binding.tvPlaylistEmptyTracks.isVisible = false
        }
    }

    private fun configureBackButton() = binding.ivBack
        .setOnClickListener { findNavController().popBackStack() }

    private fun configureShareButton() = binding.btnPlaylistShare
        .setOnClickListener { viewModel.share(playlistId) }

    private fun configureMenuButton() = binding.btnPlaylistMenu
        .setOnClickListener { menuBottomSheet.state = STATE_COLLAPSED }

    private fun configureTracksRecycler() = with(binding.rvPlaylistTracks) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = tracksAdapter
    }

    private fun configureMenuBottomSheet() {
        binding.tvSharePlaylistShare.setOnClickListener { viewModel.share(playlistId) }
        binding.tvSharePlaylistEdit.setOnClickListener { navigateToEditPlaylist(playlistId) }
        binding.tvSharePlaylistDelete.setOnClickListener {
            menuBottomSheet.state = STATE_HIDDEN
            showConfirmDeletePlaylistDialog(playlistId)
        }
    }

    private fun setPlaylistCover(
        imagePath: String,
        imageView: ImageView
    ) = Glide.with(requireContext())
        .load(imagePath)
        .placeholder(R.drawable.track_placeholder)
        .into(imageView)

    private fun navigateToPlayer(
        track: Track
    ) = findNavController().navigate(
        resId = R.id.action_playlistDetailsFragment_to_playerFragment,
        args = PlayerFragment.createArgs(track)
    )

    private fun showConfirmDeleteTrackDialog(
        playlistId: Int,
        track: Track
    ) = MaterialAlertDialogBuilder(requireContext())
        .setTitle(getString(R.string.confirm_delete_track_title))
        .setMessage(getString(R.string.confirm_delete_track_message))
        .setNegativeButton(getString(R.string.no)) { _, _ -> }
        .setPositiveButton(getString(R.string.yes)) { _, _ ->
            viewModel.deleteTrackFromPlaylist(playlistId, track)
        }.show()

    private fun showConfirmDeletePlaylistDialog(
        playlistId: Int
    ) = MaterialAlertDialogBuilder(requireContext())
        .setTitle(getString(R.string.confirm_delete_playlist_title))
        .setNegativeButton(getString(R.string.no)) { _, _ -> }
        .setPositiveButton(getString(R.string.yes)) { _, _ ->
            viewModel.deletePlaylist(playlistId)
        }.show()

    private fun showToast(text: String) =
        Toast.makeText(requireContext(), text, LENGTH_SHORT).show()

    private fun navigateToEditPlaylist(playlistId: Int) = findNavController().navigate(
        resId = R.id.action_playlistDetailsFragment_to_editPlaylistFragment,
        args = EditPlaylistFragment.createArgs(playlistId)
    )
}
