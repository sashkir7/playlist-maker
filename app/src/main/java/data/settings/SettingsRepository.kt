package data.settings

interface SettingsRepository {

    fun isDarkThemeEnabled(): Boolean

    fun saveThemeState(darkThemeEnabled: Boolean)
}