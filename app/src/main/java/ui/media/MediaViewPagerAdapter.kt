package ui.media

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ui.media.favoriteTracks.FavoriteTracksFragment
import ui.media.playlists.PlaylistsFragment

class MediaViewPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment =
        if (position == 0) FavoriteTracksFragment.newInstance()
        else PlaylistsFragment.newInstance()
}
