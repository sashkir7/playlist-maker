package di

import data.player.PlayerRepository
import data.player.PlayerRepositoryImpl
import domain.player.PlayerInteractor
import domain.player.PlayerInteractorImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ui.player.PlayerViewModel

val playerModule = module {

    viewModel<PlayerViewModel> { PlayerViewModel(get()) }

    single<PlayerInteractor> { PlayerInteractorImpl(get()) }

    single<PlayerRepository> { PlayerRepositoryImpl() }
}
