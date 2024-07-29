package domain.settings

class SettingsInteractorImpl(
    private val repository: SettingsRepository
) : SettingsInteractor {

    override fun isDarkThemeEnabled(): Boolean =
        repository.isDarkThemeEnabled()

    override fun saveThemeState(darkThemeEnabled: Boolean) =
        repository.saveThemeState(darkThemeEnabled)
}
