package creator

import android.app.Application
import android.content.Context.MODE_PRIVATE
import data.common.network.NetworkClient
import data.common.network.RetrofitNetworkClient
import data.player.PlayerRepositoryImpl
import domain.player.PlayerInteractor
import data.player.PlayerRepository
import data.search.HistoryRepository
import data.search.HistoryRepositoryImpl
import data.search.TrackRepository
import data.search.TrackRepositoryImpl
import data.settings.SettingsRepositoryImpl
import data.sharing.ExternalNavigator
import data.sharing.ExternalNavigatorImpl
import domain.player.PlayerInteractorImpl
import domain.settings.SettingsInteractor
import domain.settings.SettingsInteractorImpl
import data.settings.SettingsRepository
import domain.search.HistoryInteractor
import domain.search.HistoryInteractorImpl
import domain.search.TrackInteractor
import domain.search.TrackInteractorImpl
import domain.sharing.SharingInteractor
import domain.sharing.SharingInteractorImpl

object Creator {

    private lateinit var application: Application

    private const val HISTORY_SHARED_PREFS = "HISTORY"
    private const val SETTINGS_SHARED_PREFS = "SETTINGS"

    fun initState(application: Application) {
        this.application = application
    }

    fun providePlayerInteractor(): PlayerInteractor =
        PlayerInteractorImpl(repository = providePlayerRepository())

    fun provideTrackInteractor(): TrackInteractor =
        TrackInteractorImpl(repository = provideTrackRepository())

    fun provideHistoryInteractor(): HistoryInteractor =
        HistoryInteractorImpl(repository = provideHistoryRepository())

    fun provideSharingInteractor(): SharingInteractor =
        SharingInteractorImpl(provideExternalNavigator())

    fun provideSettingsInteractor(): SettingsInteractor =
        SettingsInteractorImpl(provideSettingsRepository())

    private fun provideExternalNavigator(): ExternalNavigator =
        ExternalNavigatorImpl(application)

    private fun provideTrackRepository(): TrackRepository =
        TrackRepositoryImpl(networkClient = provideNetworkClient())

    private fun provideSettingsRepository(): SettingsRepository =
        SettingsRepositoryImpl(
            sharedPreferences = application.getSharedPreferences(
                SETTINGS_SHARED_PREFS, MODE_PRIVATE
            )
        )

    private fun provideHistoryRepository(): HistoryRepository =
        HistoryRepositoryImpl(
            sharedPreferences = application.getSharedPreferences(
                HISTORY_SHARED_PREFS, MODE_PRIVATE
            )
        )

    private fun providePlayerRepository(): PlayerRepository = PlayerRepositoryImpl()

    private fun provideNetworkClient(): NetworkClient = RetrofitNetworkClient(application)
}
