package data.sharing

import android.content.Context
import android.content.Intent
import android.net.Uri

class ExternalNavigatorImpl(
    private val context: Context
) : ExternalNavigator {

    override fun shareLink(link: String, title: String) {
        val intent = Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT, link)
        val chooserIntent = Intent.createChooser(intent, title)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    }

    override fun openLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW)
            .setData(Uri.parse(link))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun openEmail(email: String, subject: String, text: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
            .setData(Uri.parse("mailto:"))
            .putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            .putExtra(Intent.EXTRA_SUBJECT, subject)
            .putExtra(Intent.EXTRA_TEXT, text)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
