package data.sharing

interface ExternalNavigator {

    fun shareLink(
        link: String,
        title: String
    )

    fun openLink(link: String)

    fun openEmail(
        email: String,
        subject: String,
        text: String
    )
}
