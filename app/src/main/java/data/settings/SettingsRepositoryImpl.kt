package data.settings

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import domain.settings.SettingsRepository

class SettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : SettingsRepository {

    companion object {
        private const val DARK_THEME_KEY = "DARK_THEME_ENABLED"
    }

    override fun isDarkThemeEnabled(): Boolean =
        sharedPreferences.getBoolean(DARK_THEME_KEY, false)

    override fun saveThemeState(darkThemeEnabled: Boolean) {
        val mode = if (darkThemeEnabled) MODE_NIGHT_YES else MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
        sharedPreferences.edit().putBoolean(DARK_THEME_KEY, darkThemeEnabled).apply()
    }
}
