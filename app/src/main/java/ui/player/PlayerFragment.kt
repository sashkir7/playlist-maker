package ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import domain.player.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import ui.player.PlayerState.Default
import ui.player.PlayerState.Paused
import ui.player.PlayerState.Playing
import ui.player.PlayerState.Prepared
import ui.player.PlayerTrackState.AddedToPlaylist
import ui.player.PlayerTrackState.AlreadyInPlaylist
import ui.player.PlayerTrackState.InFavorite
import ui.player.PlayerTrackState.NotInFavorite
import ui.player.PlayerTrackState.ReceivedPlaylists
import utils.cornerRadius
import utils.isVisible

class PlayerFragment : Fragment() {

    companion object {
        private const val EXTRA_TRACK = "EXTRA_TRACK_KEY"

        fun createArgs(
            track: Track
        ): Bundle = bundleOf(EXTRA_TRACK to track)
    }

    private lateinit var track: Track

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlayerViewModel by viewModel()

    private val bottomSheet: BottomSheetBehavior<LinearLayout> by lazy {
        BottomSheetBehavior.from(binding.playlistsBottomSheet)
    }

    private val playlistsAdapter by lazy {
        PlayerPlaylistAdapter { playlist ->
            viewModel.addToPlaylist(
                playlistId = checkNotNull(playlist.id),
                track = track
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        track = requireArguments().getSerializable(EXTRA_TRACK) as Track

        configureBackButton()
        configureTrackCover(track.coverArtworkUrl)
        configureTrackInformation(track)

        configurePlayButton()
        configureAddToPlaylistButton()
        configureAddToFavoriteButton(track)
        configureMediaPlayer(track.previewUrl)

        configureBottomSheet()

        viewModel.playerState.observe(viewLifecycleOwner) { render(it) }
        viewModel.trackState.observe(viewLifecycleOwner) { render(it) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.init(track)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: PlayerState) {
        val playOrPauseButton = binding.ivPlayButton
        val trackCurrentPosition = binding.tvTrackCurrentPosition

        when (state) {
            is Default, Paused -> playOrPauseButton.setImageResource(R.drawable.play_icon)
            is Prepared -> {
                playOrPauseButton.setImageResource(R.drawable.play_icon)
                trackCurrentPosition.text = getString(R.string.track_duration_zero)
            }

            is Playing -> {
                playOrPauseButton.setImageResource(R.drawable.pause_icon)
                trackCurrentPosition.text = state.currentPosition
            }
        }
    }

    private fun render(state: PlayerTrackState) {
        when (state) {
            is AddedToPlaylist -> {
                bottomSheet.state = STATE_HIDDEN
                showToast(getString(R.string.track_added_to_playlist, state.playlist.name))
            }

            is AlreadyInPlaylist ->
                showToast(getString(R.string.track_dont_added_to_playlist, state.playlist.name))

            is InFavorite ->
                binding.ivAddToFavorite.setImageResource(R.drawable.added_track_to_favorite)

            is NotInFavorite ->
                binding.ivAddToFavorite.setImageResource(R.drawable.add_track_to_favorite)

            is ReceivedPlaylists -> playlistsAdapter.setPlaylists(state.playlists)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }

    private fun configureBackButton() = binding.ivBack.setOnClickListener {
        findNavController().popBackStack()
    }

    private fun configureTrackCover(url: String) {
        Glide.with(requireContext())
            .load(url)
            .cornerRadius(value = 8)
            .placeholder(R.drawable.track_placeholder)
            .into(binding.ivTrackCover)
    }

    private fun configurePlayButton() = binding.ivPlayButton.setOnClickListener {
        when (viewModel.playerState.value) {
            is Playing -> viewModel.pause()
            is Prepared, Paused -> viewModel.start()
            else -> showToast(getString(R.string.media_player_is_not_prepared))
        }
    }

    private fun configureTrackInformation(track: Track) {
        binding.tvTrackName.text = track.trackName
        binding.tvArtistName.text = track.artistName

        binding.tvDurationValue.text = track.trackTimeAsString
        configureTextView(
            binding.tvCollectionNameValue,
            binding.grCollectionName,
            track.collectionName
        )
        configureTextView(
            binding.tvReleaseDateValue,
            binding.grReleaseDate,
            track.releaseDateYear?.toString()
        )
        configureTextView(binding.tvGenreNameValue, binding.grGenreName, track.genreName)
        configureTextView(binding.tvCountryValue, binding.grCountry, track.country)
    }

    private fun configureTextView(textView: TextView, groupView: View, value: String?) {
        textView.text = value
        groupView.isVisible = value != null
    }

    private fun configureAddToPlaylistButton() =
        binding.ivAddToPlaylist.setOnClickListener {
            bottomSheet.state = STATE_COLLAPSED
        }

    private fun configureAddToFavoriteButton(track: Track) =
        binding.ivAddToFavorite.setOnClickListener {
            viewModel.onFavoriteClicked(track)
        }

    private fun configureMediaPlayer(previewUrl: String) = viewModel.prepare(previewUrl)

    private fun showToast(text: String) =
        Toast.makeText(requireContext(), text, LENGTH_SHORT).show()

    private fun configureBottomSheet() {
        bottomSheet.state = STATE_HIDDEN

        with(binding.recyclerPlaylists) {
            adapter = playlistsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.btnAddPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment)
        }
    }
}
