package fr.xgouchet.packageexplorer.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import fr.xgouchet.packageexplorer.R
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element


class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val aboutPage = AboutPage(this)
                .isRTL(false)
                .setDescription(getString(R.string.about_description))
                .setImage(R.drawable.about_header)
                .addPlayStore(packageName)

                .addGroup(getString(R.string.about_contact))
                .addEmail(getString(R.string.about_email), getString(R.string.about_email))
                .addTwitter(getString(R.string.about_twitter))
                .addGitHub(getString(R.string.about_github))

                .addGroup(getString(R.string.about_license))
                .addItem(Element(getString(R.string.about_copyright), R.drawable.ic_about_copyright))
                .addItem(Element(getString(R.string.about_mit), R.drawable.ic_about_opensource))
                .addItem(Element(getString(R.string.about_donate), R.drawable.ic_about_donate).apply {
                    setOnClickListener {
                        val uri = Uri.parse(getString(R.string.about_donate_url))
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }
                })

                .addGroup(getString(R.string.about_history))
                .addItem(Element(getString(R.string.about_history_v2_3), null))
                .addItem(Element(getString(R.string.about_history_v2_2), null))
                .addItem(Element(getString(R.string.about_history_v2_1), null))
                .addItem(Element(getString(R.string.about_history_v2), null))
                .addItem(Element(getString(R.string.about_history_v1), null))
                .create()

        setContentView(aboutPage)

    }

}