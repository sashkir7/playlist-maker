package di

import data.db.AppDatabase
import data.media.playlists.PlaylistRepository
import data.media.playlists.PlaylistRepositoryImpl
import domain.media.PlaylistInteractor
import domain.media.PlaylistInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ui.media.favoriteTracks.FavoriteTracksViewModel
import ui.media.playlists.all.PlaylistsViewModel
import ui.media.playlists.details.PlaylistDetailsViewModel
import ui.media.playlists.modify.create.CreatePlaylistViewModel
import ui.media.playlists.modify.edit.EditPlaylistViewModel

val mediaModule = module {

    viewModel<FavoriteTracksViewModel> { FavoriteTracksViewModel(get()) }
    viewModel<CreatePlaylistViewModel> { CreatePlaylistViewModel(get()) }
    viewModel<EditPlaylistViewModel> { EditPlaylistViewModel(get()) }
    viewModel<PlaylistsViewModel> { PlaylistsViewModel(get()) }
    viewModel<PlaylistDetailsViewModel> { PlaylistDetailsViewModel(get(), get()) }

    single<PlaylistInteractor> { PlaylistInteractorImpl(get()) }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(
            context = androidContext(),
            playlistDao = get<AppDatabase>().playlistDao(),
            trackDao = get<AppDatabase>().playlistTracksDao(),
            playlistConvertor = get(),
            trackConvertor = get()
        )
    }
}
