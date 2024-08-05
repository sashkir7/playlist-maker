package di

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import data.settings.SettingsRepository
import data.settings.SettingsRepositoryImpl
import data.sharing.ExternalNavigator
import data.sharing.ExternalNavigatorImpl
import domain.settings.SettingsInteractor
import domain.settings.SettingsInteractorImpl
import domain.sharing.SharingInteractor
import domain.sharing.SharingInteractorImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ui.settings.SettingsViewModel

private const val SETTINGS_SHARED_PREFS = "SETTINGS"

val settingsModule = module {

    viewModel<SettingsViewModel> {
        SettingsViewModel(
            application = androidApplication(),
            sharingInteractor = get(),
            settingsInteractor = get()
        )
    }

    single<SharingInteractor> { SharingInteractorImpl(get()) }
    single<SettingsInteractor> { SettingsInteractorImpl(get()) }

    single<SettingsRepository> { SettingsRepositoryImpl(get()) }

    single<ExternalNavigator> { ExternalNavigatorImpl(androidContext()) }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            SETTINGS_SHARED_PREFS, MODE_PRIVATE
        )
    }
}
