package creator

import android.app.Application
import android.content.Context.MODE_PRIVATE
import data.common.network.NetworkClient
import data.common.network.RetrofitNetworkClient
import data.player.PlayerRepositoryImpl
import domain.player.PlayerInteractor
import data.player.PlayerRepository
import data.search.SearchRepository
import data.search.SearchRepositoryImpl
import data.settings.SettingsRepositoryImpl
import data.sharing.ExternalNavigator
import data.sharing.ExternalNavigatorImpl
import domain.player.PlayerInteractorImpl
import domain.settings.SettingsInteractor
import domain.settings.SettingsInteractorImpl
import data.settings.SettingsRepository
import domain.search.SearchInteractor
import domain.search.SearchInteractorImpl
import domain.sharing.SharingInteractor
import domain.sharing.SharingInteractorImpl

object Creator {

    private lateinit var application: Application

    private const val HISTORY_SHARED_PREFS = "HISTORY"

    fun initState(application: Application) {
        this.application = application
    }

    fun provideSearchInteractor(): SearchInteractor =
        SearchInteractorImpl(repository = provideHistoryRepository())

    private fun provideHistoryRepository(): SearchRepository =
        SearchRepositoryImpl(
            networkClient = provideNetworkClient(),
            sharedPreferences = application.getSharedPreferences(
                HISTORY_SHARED_PREFS, MODE_PRIVATE
            )
        )

    private fun provideNetworkClient(): NetworkClient = RetrofitNetworkClient(application)
}
