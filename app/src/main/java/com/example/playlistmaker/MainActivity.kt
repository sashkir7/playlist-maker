package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureMenuButtons()
    }

    private fun configureMenuButtons() {
        val searchButton = findViewById<Button>(R.id.search_button)
        val searchButtonListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                createToast("Выполняется поиск").show()
            }
        }
        searchButton.setOnClickListener(searchButtonListener)

        val mediaButton = findViewById<Button>(R.id.media_button)
        val mediaButtonListener = View.OnClickListener {
            createToast("Отображается медиатека").show()
        }
        mediaButton.setOnClickListener(mediaButtonListener)

        val settingsButton = findViewById<Button>(R.id.settings_button)
        settingsButton.setOnClickListener {
            createToast("Просматриваются настройки").show()
        }
    }

    private fun createToast(text: String) = Toast.makeText(
        this@MainActivity, text, Toast.LENGTH_SHORT
    )
}
