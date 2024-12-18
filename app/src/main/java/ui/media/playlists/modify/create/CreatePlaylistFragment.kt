package ui.media.playlists.modify.create

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import com.example.playlistmaker.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import domain.media.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel
import ui.media.playlists.modify.ModifyPlaylistFragment

class CreatePlaylistFragment : ModifyPlaylistFragment() {

    private val viewModel: CreatePlaylistViewModel by viewModel()

    private val confirmDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.add_new_playlist_dialog_title))
            .setMessage(getString(R.string.add_new_playlist_dialog_message))
            .setNeutralButton(getString(R.string.add_new_playlist_dialog_cancel)) { _, _ -> }
            .setPositiveButton(
                getString(R.string.add_new_playlist_dialog_finish)
            ) { _, _ -> goToBackScreen() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCustomBackPressedDispatcher()

        binding.tvFragmentTitle.text = getString(R.string.add_new_playlist)
        binding.btnModifyPlaylist.text = getString(R.string.create_title)

        binding.btnModifyPlaylist.setOnClickListener {
            val playlist = createPlaylist()
            goToBackScreen()
            showToast(getString(R.string.add_new_playlist_playlist_created, playlist.name))
        }
    }

    private fun setCustomBackPressedDispatcher() = requireActivity()
        .onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val needShowConfirmDialog = binding.etTitle.text.toString().isNotBlank() ||
                    binding.etDescription.text.toString().isNotBlank() ||
                    binding.ivPlaylistCover.tag != null

            if (needShowConfirmDialog) confirmDialog.show()
            else goToBackScreen()
        }

    private fun createPlaylist(): Playlist {
        val title = binding.etTitle.text.toString()
        val description = binding.etDescription.text?.toString()
        val pathToImage = (binding.ivPlaylistCover.tag as? Uri)
            ?.let { viewModel.saveImage(it, title) }

        val playlist = Playlist(
            name = title,
            description = description,
            pathToImage = pathToImage
        )
        viewModel.createPlaylist(playlist)

        return playlist
    }
}
