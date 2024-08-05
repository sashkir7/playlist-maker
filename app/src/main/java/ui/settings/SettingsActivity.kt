package ui.settings

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val viewModel: SettingsViewModel by viewModel()

    private val themeSwitcher by lazy { findViewById<SwitchCompat>(R.id.themeSwitcher) }
    private val backButton by lazy { findViewById<ImageView>(R.id.backButton) }
    private val shareButton by lazy { findViewById<View>(R.id.share_app_button) }
    private val supportButton by lazy { findViewById<View>(R.id.support_button) }
    private val agreementButton by lazy { findViewById<View>(R.id.agreement_button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        configureThemeSwitcher()
        configureBackButton()

        configureShareButton()
        configureWriteToSupportButton()
        configureAgreementButton()
    }

    private fun configureThemeSwitcher() = with(themeSwitcher) {
        viewModel.darkThemeEnabled.observe(this@SettingsActivity) { isChecked = it }
        setOnCheckedChangeListener { _, isChecked -> viewModel.switchTheme(isChecked) }
    }

    private fun configureBackButton() =
        backButton.setOnClickListener { onBackPressed() }

    private fun configureShareButton() =
        shareButton.setOnClickListener { viewModel.shareApp() }

    private fun configureWriteToSupportButton() =
        supportButton.setOnClickListener { viewModel.openSupport() }

    private fun configureAgreementButton() =
        agreementButton.setOnClickListener { viewModel.openTerms() }
}
