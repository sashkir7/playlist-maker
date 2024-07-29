package domain.sharing

import data.sharing.ExternalNavigator

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
) : SharingInteractor {

    override fun shareApp(link: String, title: String) =
        externalNavigator.shareLink(link, title)

    override fun openTerms(link: String) =
        externalNavigator.openLink(link)

    override fun openSupport(email: String, subject: String, text: String) =
        externalNavigator.openEmail(email, subject, text)
}
