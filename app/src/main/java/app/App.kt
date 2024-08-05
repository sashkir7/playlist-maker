package app

import android.app.Application
import creator.Creator
import di.playerModule
import di.settingsModule
import domain.settings.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    private var darkThemeEnabled = false

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(settingsModule, playerModule)
        }

        Creator.initState(this)
        initThemeState()
    }

    private fun initThemeState() {
        val settingsInteractor by inject<SettingsInteractor>()
        with(settingsInteractor) {
            darkThemeEnabled = isDarkThemeEnabled()
            saveThemeState(darkThemeEnabled)
        }
    }
}
