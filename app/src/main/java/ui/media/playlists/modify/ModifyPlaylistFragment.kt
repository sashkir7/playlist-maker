package ui.media.playlists.modify

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView.ScaleType.CENTER_CROP
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding

abstract class ModifyPlaylistFragment : Fragment() {

    private var _binding: FragmentNewPlaylistBinding? = null
    protected val binding get() = _binding!!

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

        configureBackButton()
        configureCoverImageView()
        configureTitleEditText()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun goToBackScreen() = findNavController().popBackStack()

    protected fun showToast(text: String) =
        Toast.makeText(requireContext(), text, LENGTH_SHORT).show()

    private fun configureBackButton() = binding.ivBack
        .setOnClickListener { findNavController().popBackStack() }

    private fun configureCoverImageView() = binding.ivPlaylistCover
        .setOnClickListener { photoPicker.launch(PickVisualMediaRequest(ImageOnly)) }

    private fun configureTitleEditText() =
        binding.etTitle.doOnTextChanged { text, _, _, _ ->
            binding.btnModifyPlaylist.isEnabled = text.toString().isNotBlank()
        }
}
