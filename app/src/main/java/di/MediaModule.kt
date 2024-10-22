package di

import data.db.AppDatabase
import data.media.playlists.new.PlaylistRepository
import data.media.playlists.new.PlaylistRepositoryImpl
import domain.media.playlists.new.NewPlaylistInteractor
import domain.media.playlists.new.NewPlaylistInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ui.media.favoriteTracks.FavoriteTracksViewModel
import ui.media.playlists.PlaylistsViewModel
import ui.media.playlists.new.NewPlaylistViewModel

val mediaModule = module {

    viewModel<FavoriteTracksViewModel> { FavoriteTracksViewModel(get()) }
    viewModel<NewPlaylistViewModel> { NewPlaylistViewModel(get()) }
    viewModel<PlaylistsViewModel> { PlaylistsViewModel() }

    single<NewPlaylistInteractor> { NewPlaylistInteractorImpl(get()) }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(
            context = androidContext(),
            playlistDao = get<AppDatabase>().playlistDao(),
            convertor = get()
        )
    }
}
