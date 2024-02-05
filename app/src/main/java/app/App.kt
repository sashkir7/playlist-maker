package app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import storage.SETTINGS_SHARED_PREFS
import storage.SettingsStorage

class App : Application() {

    var darkThemeEnabled = false

    private val settingsStorage by lazy {
        SettingsStorage(getSharedPreferences(SETTINGS_SHARED_PREFS, MODE_PRIVATE)) }

    override fun onCreate() {
        super.onCreate()

        darkThemeEnabled = settingsStorage.isDarkThemeEnabled()
        switchTheme(darkThemeEnabled)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        this.darkThemeEnabled = darkThemeEnabled
        settingsStorage.saveThemeState(darkThemeEnabled)

        val mode = if (darkThemeEnabled) MODE_NIGHT_YES else MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
