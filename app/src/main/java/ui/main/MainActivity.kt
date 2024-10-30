package ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import utils.isVisible

class MainActivity : AppCompatActivity() {

    private val menu: BottomNavigationView by lazy { findViewById(R.id.bottomNavigationView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureMenu()
    }

    private fun configureMenu() {
        val hostFragment = supportFragmentManager
            .findFragmentById(R.id.container_view) as NavHostFragment
        val controller = hostFragment.navController

        menu.setupWithNavController(controller)
        controller.addOnDestinationChangedListener { _, destination, _ ->
            menu.isVisible = !listOf(
                R.id.playerFragment,
                R.id.newPlaylistFragment,
                R.id.editPlaylistFragment,
                R.id.playlistDetailsFragment
            ).contains(destination.id)
        }
    }
}
