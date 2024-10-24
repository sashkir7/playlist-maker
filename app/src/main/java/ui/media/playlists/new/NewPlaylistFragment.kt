package ui.media.playlists.new

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView.ScaleType.CENTER_CROP
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import domain.media.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment() {

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewPlaylistViewModel by viewModel()

    private val photoPicker =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri == null) {
                Log.d("PhotoPicker", "No media selected")
                return@registerForActivityResult
            }

            with(binding.ivPlaylistCover) {
                scaleType = CENTER_CROP
                tag = uri
                setImageURI(uri)
            }
        }

    private val confirmDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.add_new_playlist_dialog_title))
            .setMessage(getString(R.string.add_new_playlist_dialog_message))
            .setNeutralButton(getString(R.string.add_new_playlist_dialog_cancel)) { _, _ -> }
            .setPositiveButton(
                getString(R.string.add_new_playlist_dialog_finish)
            ) { _, _ -> goToBackScreen() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCustomBackPressedDispatcher()

        configureBackButton()
        configureCoverImageView()
        configureTitleEditText()
        configureCreatePlaylistButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setCustomBackPressedDispatcher() = requireActivity()
        .onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val needShowConfirmDialog = binding.etTitle.text.toString().isNotBlank() ||
                    binding.etDescription.text.toString().isNotBlank() ||
                    binding.ivPlaylistCover.tag != null

            if (needShowConfirmDialog) {
                confirmDialog.show()
            } else {
                goToBackScreen()
            }
        }

    private fun configureBackButton() = binding.ivBack.setOnClickListener { goToBackScreen() }

    private fun configureCoverImageView() = binding.ivPlaylistCover
        .setOnClickListener { photoPicker.launch(PickVisualMediaRequest(ImageOnly)) }

    private fun configureTitleEditText() =
        binding.etTitle.doOnTextChanged { text, _, _, _ ->
            binding.btnCreatePlaylist.isEnabled = text.toString().isNotBlank()
        }

    private fun configureCreatePlaylistButton() = binding.btnCreatePlaylist.setOnClickListener {
        val playlist = createPlaylist()
        goToBackScreen()
        showToast(getString(R.string.add_new_playlist_playlist_created, playlist.name))
    }

    private fun createPlaylist(): Playlist {
        val title = binding.etTitle.text.toString()
        val description = binding.etDescription.text?.toString()
        val pathToImage = (binding.ivPlaylistCover.tag as? Uri)
            ?.let { viewModel.saveImage(it, title) }

        val playlist = Playlist(name = title, description = description, pathToImage = pathToImage)
        viewModel.createPlaylist(playlist)

        return playlist
    }

    private fun goToBackScreen() = findNavController().popBackStack()

    private fun showToast(text: String) =
        Toast.makeText(requireContext(), text, LENGTH_SHORT).show()
}
