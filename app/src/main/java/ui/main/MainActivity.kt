package ui.main

import ui.media.MediaActivity
import ui.search.SearchHistoryActivity
import ui.settings.SettingsActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.R

class MainActivity : AppCompatActivity() {

    private val searchButton by lazy { findViewById<Button>(R.id.search_button) }
    private val mediaButton by lazy { findViewById<Button>(R.id.media_button) }
    private val settingsButton by lazy { findViewById<Button>(R.id.settings_button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureSearchButton()
        configureMediaButton()
        configureSettingsButton()
    }

    private fun configureSearchButton() = searchButton.setOnClickListener {
        startActivity(Intent(this, SearchHistoryActivity::class.java))
    }

    private fun configureMediaButton() = mediaButton.setOnClickListener {
        startActivity(Intent(this, MediaActivity::class.java))
    }

    private fun configureSettingsButton() = settingsButton.setOnClickListener {
        startActivity(Intent(this, SettingsActivity::class.java))
    }
}
