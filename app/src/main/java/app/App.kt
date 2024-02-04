package app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import dataSource.SETTINGS_SHARED_PREFERENCES
import dataSource.SettingsDataSource

class App : Application() {

    var darkThemeEnabled = false

    private val settingsDataSource by lazy {
        SettingsDataSource(
            getSharedPreferences(SETTINGS_SHARED_PREFERENCES, MODE_PRIVATE)
        )
    }

    override fun onCreate() {
        super.onCreate()

        darkThemeEnabled = settingsDataSource.isDarkThemeEnabled()
        switchTheme(darkThemeEnabled)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        this.darkThemeEnabled = darkThemeEnabled
        settingsDataSource.saveThemeState(darkThemeEnabled)

        val mode = if (darkThemeEnabled) MODE_NIGHT_YES else MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
