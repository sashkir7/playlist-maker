package creator

import android.app.Application
import android.content.Context.MODE_PRIVATE
import data.player.PlayerRepositoryImpl
import domain.player.PlayerInteractor
import data.player.PlayerRepository
import data.settings.SettingsRepositoryImpl
import data.sharing.ExternalNavigator
import data.sharing.ExternalNavigatorImpl
import domain.player.PlayerInteractorImpl
import domain.settings.SettingsInteractor
import domain.settings.SettingsInteractorImpl
import domain.settings.SettingsRepository
import domain.sharing.SharingInteractor
import domain.sharing.SharingInteractorImpl

object Creator {

    private lateinit var application: Application

    private const val SETTINGS_SHARED_PREFS = "SETTINGS"

    fun initState(application: Application) {
        this.application = application
    }

    fun providePlayerRepository(): PlayerRepository = PlayerRepositoryImpl()

    fun providePlayerInteractor(): PlayerInteractor =
        PlayerInteractorImpl(repository = providePlayerRepository())

    fun provideSharingInteractor(): SharingInteractor =
        SharingInteractorImpl(provideExternalNavigator())

    fun provideSettingsInteractor(): SettingsInteractor =
        SettingsInteractorImpl(provideSettingsRepository())

    private fun provideExternalNavigator(): ExternalNavigator =
        ExternalNavigatorImpl(application)

    private fun provideSettingsRepository(): SettingsRepository =
        SettingsRepositoryImpl(
            application.getSharedPreferences(
                SETTINGS_SHARED_PREFS,
                MODE_PRIVATE
            )
        )
}
