package ui.media

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.playlistmaker.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MediaActivity : AppCompatActivity() {

    private lateinit var tabMediator: TabLayoutMediator

    private val backButton by lazy { findViewById<ImageView>(R.id.backButton) }
    private val tabLayout by lazy { findViewById<TabLayout>(R.id.mediaTabLayout) }
    private val viewPager by lazy { findViewById<ViewPager2>(R.id.mediaViewPager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        configureBackButton()
        configureViewPager()
        configureTabMediator()

        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

    private fun configureBackButton(): Unit = backButton.setOnClickListener { onBackPressed() }

    private fun configureViewPager() {
        viewPager.adapter = MediaViewPagerAdapter(supportFragmentManager, lifecycle)
    }

    private fun configureTabMediator() {
        tabMediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val stringResources = if (position == 0) {
                R.string.favorite_tracks_title
            } else R.string.playlists_title
            tab.text = getString(stringResources)
        }
    }
}
