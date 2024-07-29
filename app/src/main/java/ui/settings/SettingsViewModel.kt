package ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.R
import creator.Creator
import domain.settings.SettingsInteractor
import domain.sharing.SharingInteractor

class SettingsViewModel(
    private val application: Application,
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : AndroidViewModel(application) {

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    application = this[APPLICATION_KEY] as Application,
                    sharingInteractor = Creator.provideSharingInteractor(),
                    settingsInteractor = Creator.provideSettingsInteractor()
                )
            }
        }
    }

    private var mutableDarkThemeEnabled = MutableLiveData<Boolean>()
    val darkThemeEnabled: LiveData<Boolean>
        get() = mutableDarkThemeEnabled

    init {
        mutableDarkThemeEnabled.postValue(settingsInteractor.isDarkThemeEnabled())
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        mutableDarkThemeEnabled.postValue(darkThemeEnabled)
        settingsInteractor.saveThemeState(darkThemeEnabled)
    }

    fun shareApp() = sharingInteractor.shareApp(
        link = application.getString(R.string.share_application),
        title = application.getString(R.string.android_developer_url)
    )

    fun openSupport() = sharingInteractor.openSupport(
        email = application.getString(R.string.student_email),
        subject = application.getString(R.string.write_to_support_email_subject),
        text = application.getString(R.string.write_to_support_email_body)
    )

    fun openTerms() = sharingInteractor.openTerms(
        link = application.getString(R.string.agreement_url)
    )
}