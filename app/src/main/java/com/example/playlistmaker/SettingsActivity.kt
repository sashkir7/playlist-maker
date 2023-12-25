package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        configureShareApplicationButton()
        configureWriteToSupportButton()
        configureAgreementButton()
    }

    private fun configureShareApplicationButton() {
        val view = findViewById<View>(R.id.share_app_button)
        view.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developer_url))
            startActivity(Intent.createChooser(intent, getString(R.string.share_application)))
        }
    }

    private fun configureWriteToSupportButton() {
        val view = findViewById<View>(R.id.support_button)
        view.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
                .setData(Uri.parse("mailto:"))
                .putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.student_email)))
                .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.write_to_support_email_subject))
                .putExtra(Intent.EXTRA_TEXT, getString(R.string.write_to_support_email_body))
            startActivity(intent)
        }
    }

    private fun configureAgreementButton() {
        val view = findViewById<View>(R.id.agreement_button)
        view.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse(getString(R.string.agreement_url)))
            startActivity(intent)
        }
    }
}