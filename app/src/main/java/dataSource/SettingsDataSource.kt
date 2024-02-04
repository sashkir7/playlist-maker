package dataSource

import android.content.SharedPreferences

const val SETTINGS_SHARED_PREFERENCES = "SETTINGS"

class SettingsDataSource(
    private val sharedPrefs: SharedPreferences
) {

    companion object {
        private const val DARK_THEME_KEY = "DARK_THEME_ENABLED"
    }

    fun isDarkThemeEnabled(): Boolean =
        sharedPrefs.getBoolean(DARK_THEME_KEY, false)

    fun saveThemeState(darkThemeEnabled: Boolean): Unit =
        sharedPrefs.edit()
            .putBoolean(DARK_THEME_KEY, darkThemeEnabled)
            .apply()
}
