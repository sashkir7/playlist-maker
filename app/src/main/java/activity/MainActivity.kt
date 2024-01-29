package activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.playlistmaker.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureMenuButtons()
    }

    private fun configureMenuButtons() {
        val searchButton = findViewById<Button>(R.id.search_button)
        configureStartActivityOnClick(searchButton, SearchActivity::class.java)

        val mediaButton = findViewById<Button>(R.id.media_button)
        configureStartActivityOnClick(mediaButton, MediaActivity::class.java)

        val settingsButton = findViewById<Button>(R.id.settings_button)
        configureStartActivityOnClick(settingsButton, SettingsActivity::class.java)
    }

    private fun configureStartActivityOnClick(view: View, activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        view.setOnClickListener { startActivity(intent) }
    }
}
