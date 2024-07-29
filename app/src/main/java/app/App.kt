package app

import android.app.Application
import creator.Creator

class App : Application() {

    private var darkThemeEnabled = false

    override fun onCreate() {
        super.onCreate()
        Creator.initState(this)
        initThemeState()
    }

    private fun initThemeState() =
        with(Creator.provideSettingsInteractor()) {
            darkThemeEnabled = isDarkThemeEnabled()
            saveThemeState(darkThemeEnabled)
        }
}
