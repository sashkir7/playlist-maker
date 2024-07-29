package domain.settings

interface SettingsInteractor {

    fun isDarkThemeEnabled(): Boolean

    fun saveThemeState(darkThemeEnabled: Boolean)
}
