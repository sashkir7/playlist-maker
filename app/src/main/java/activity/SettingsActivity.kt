package activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import app.App
import com.example.playlistmaker.R

class SettingsActivity : AppCompatActivity() {

    private val backButton by lazy { findViewById<ImageView>(R.id.backButton) }

    private val themeSwitcher by lazy { findViewById<SwitchCompat>(R.id.themeSwitcher) }
    private val shareApplicationButton by lazy { findViewById<View>(R.id.share_app_button) }
    private val supportButton by lazy { findViewById<View>(R.id.support_button) }
    private val agreementButton by lazy { findViewById<View>(R.id.agreement_button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        configureThemeSwitcher()
        configureBackButton()
        configureShareApplicationButton()
        configureWriteToSupportButton()
        configureAgreementButton()
    }

    private fun configureThemeSwitcher() {
        themeSwitcher.isChecked = (applicationContext as App).darkThemeEnabled
        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
        }
    }

    private fun configureBackButton(): Unit = backButton.setOnClickListener { onBackPressed() }

    private fun configureShareApplicationButton() = shareApplicationButton.setOnClickListener {
        val intent = Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developer_url))
        startActivity(Intent.createChooser(intent, getString(R.string.share_application)))
    }


    private fun configureWriteToSupportButton() = supportButton.setOnClickListener {
        val intent = Intent(Intent.ACTION_SENDTO)
            .setData(Uri.parse("mailto:"))
            .putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.student_email)))
            .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.write_to_support_email_subject))
            .putExtra(Intent.EXTRA_TEXT, getString(R.string.write_to_support_email_body))
        startActivity(intent)
    }


    private fun configureAgreementButton() = agreementButton.setOnClickListener {
        val intent = Intent(Intent.ACTION_VIEW)
            .setData(Uri.parse(getString(R.string.agreement_url)))
        startActivity(intent)
    }
}