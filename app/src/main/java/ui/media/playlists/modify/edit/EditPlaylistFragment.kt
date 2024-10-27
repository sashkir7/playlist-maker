package ui.media.playlists.modify.edit

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import domain.media.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel
import ui.media.playlists.modify.ModifyPlaylistFragment

class EditPlaylistFragment : ModifyPlaylistFragment() {

    companion object {
        private const val EXTRA_PLAYLIST_ID = "EXTRA_PLAYLIST_ID_KEY"

        fun createArgs(
            playlistId: Int
        ): Bundle = bundleOf(EXTRA_PLAYLIST_ID to playlistId)
    }

    private val viewModel: EditPlaylistViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViews()

        val playlistId = requireArguments().getInt(EXTRA_PLAYLIST_ID)
        viewModel.state.observe(viewLifecycleOwner) { render(it) }
        viewModel.getPlaylist(playlistId)
    }

    private fun configureViews() = with(binding) {
        ivPlaylistCover.scaleType = ImageView.ScaleType.CENTER
        tvFragmentTitle.text = getString(R.string.edit_information)
        btnModifyPlaylist.text = getString(R.string.save_title)

        btnModifyPlaylist.setOnClickListener {
            updatePlaylist()
            findNavController().navigateUp()
        }
    }

    private fun render(playlist: Playlist) {
        playlist.pathToImage?.let { setPlaylistCover(it) }
        binding.etTitle.setText(playlist.name)
        binding.etDescription.setText(playlist.description)
    }

    private fun setPlaylistCover(
        imagePath: String,
    ) = Glide.with(requireContext())
        .load(imagePath)
        .placeholder(R.drawable.track_placeholder)
        .into(binding.ivPlaylistCover)

    private fun updatePlaylist() {
        val title = binding.etTitle.text.toString()
        val description = binding.etDescription.text?.toString()
        val pathToImage = (binding.ivPlaylistCover.tag as? Uri)
            ?.let { viewModel.saveImage(it, title) }

        viewModel.updatePlaylist(title, description, pathToImage)
    }
}
