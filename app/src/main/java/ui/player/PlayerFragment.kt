package ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import domain.player.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import ui.player.PlayerState.Default
import ui.player.PlayerState.Paused
import ui.player.PlayerState.Playing
import ui.player.PlayerState.Prepared
import utils.cornerRadius
import utils.isVisible

class PlayerFragment : Fragment() {

    companion object {
        private const val EXTRA_TRACK = "EXTRA_TRACK_KEY"

        fun createArgs(
            track: Track
        ): Bundle = bundleOf(EXTRA_TRACK to track)
    }

    private lateinit var binding: FragmentPlayerBinding
    private val viewModel: PlayerViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val track = requireArguments()
            .getSerializable(EXTRA_TRACK) as Track

        configureBackButton()
        configureTrackCover(track.coverArtworkUrl)
        configureTrackInformation(track)

        configurePlayButton()
        configureMediaPlayer(track.previewUrl)

        viewModel.state.observe(viewLifecycleOwner) { render(it) }
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
        when (viewModel.state.value) {
            is Playing -> viewModel.pause()
            is Prepared, Paused -> viewModel.start()
            else -> showPlayerIsNotPreparedToast()
        }
    }

    private fun configureTrackInformation(track: Track) {
        binding.tvTrackName.text = track.trackName
        binding.tvArtistName.text = track.artistName

        binding.tvDurationValue.text = track.trackTimeAsString
        configureTextView(binding.tvCollectionNameValue, binding.grCollectionName, track.collectionName)
        configureTextView(binding.tvReleaseDateValue, binding.grReleaseDate, track.releaseDateYear?.toString())
        configureTextView(binding.tvGenreNameValue, binding.grGenreName, track.genreName)
        configureTextView(binding.tvCountryValue, binding.grCountry, track.country)
    }

    private fun configureTextView(textView: TextView, groupView: View, value: String?) {
        textView.text = value
        groupView.isVisible = value != null
    }

    private fun configureMediaPlayer(previewUrl: String) = viewModel.prepare(previewUrl)

    private fun showPlayerIsNotPreparedToast() {
        val text = getText(R.string.media_player_is_not_prepared)
        Toast.makeText(requireContext(), text, LENGTH_SHORT).show()
    }
}
