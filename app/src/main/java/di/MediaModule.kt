package di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ui.media.favoriteTracks.FavoriteTracksViewModel
import ui.media.playlists.PlaylistsViewModel

val mediaModule = module {

    viewModel<FavoriteTracksViewModel> { FavoriteTracksViewModel(get()) }

    viewModel<PlaylistsViewModel> { PlaylistsViewModel() }

}
