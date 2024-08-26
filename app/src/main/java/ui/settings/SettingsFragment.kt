package ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureThemeSwitcher()
        configureShareButton()
        configureWriteToSupportButton()
        configureAgreementButton()
    }

    private fun configureThemeSwitcher() = with(binding.themeSwitcher) {
        viewModel.darkThemeEnabled.observe(this@SettingsFragment) { isChecked = it }
        setOnCheckedChangeListener { _, isChecked -> viewModel.switchTheme(isChecked) }
    }

    private fun configureShareButton() =
        binding.shareAppButton.setOnClickListener { viewModel.shareApp() }

    private fun configureWriteToSupportButton() =
        binding.supportButton.setOnClickListener { viewModel.openSupport() }

    private fun configureAgreementButton() =
        binding.agreementButton.setOnClickListener { viewModel.openTerms() }
}