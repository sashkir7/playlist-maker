package ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.R
import domain.settings.SettingsInteractor
import domain.sharing.SharingInteractor

class SettingsViewModel(
    private val application: Application,
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : AndroidViewModel(application) {

    private val mutableDarkThemeEnabled = MutableLiveData<Boolean>()
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