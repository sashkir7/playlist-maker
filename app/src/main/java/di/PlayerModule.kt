package di

import data.db.AppDatabase
import data.player.FavoriteRepository
import data.player.FavoriteRepositoryImpl
import data.player.PlayerRepository
import data.player.PlayerRepositoryImpl
import domain.player.FavoriteInteractor
import domain.player.FavoriteInteractorImpl
import domain.player.PlayerInteractor
import domain.player.PlayerInteractorImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ui.player.PlayerViewModel

val playerModule = module {

    viewModel<PlayerViewModel> { PlayerViewModel(get(), get()) }

    single<PlayerInteractor> { PlayerInteractorImpl(get()) }
    single<FavoriteInteractor> { FavoriteInteractorImpl(get()) }

    single<PlayerRepository> { PlayerRepositoryImpl() }
    single<FavoriteRepository> {
        FavoriteRepositoryImpl(
            favoriteDao = get<AppDatabase>().favoriteTracksDao(),
            convertor = get()
        )
    }
}
