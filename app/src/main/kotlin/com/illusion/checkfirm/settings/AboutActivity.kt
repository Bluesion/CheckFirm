package com.illusion.checkfirm.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.Settings
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.illusion.checkfirm.BuildConfig
import com.illusion.checkfirm.R
import com.illusion.checkfirm.dialogs.LegalDialog
import com.illusion.checkfirm.utils.Tools
import org.jsoup.Jsoup
import java.io.IOException
import java.lang.ref.WeakReference

class AboutActivity : AppCompatActivity() {

    private lateinit var latest: MaterialTextView
    private lateinit var update: MaterialButton
    private lateinit var progress: ProgressBar
    private var latestVersion = 0

    private val mHandler = MyHandler(this@AboutActivity)

    private class MyHandler(activity: AboutActivity) : Handler() {
        private val mActivity: WeakReference<AboutActivity> = WeakReference(activity)

        override fun handleMessage(msg: Message) {
            val activity = mActivity.get()
            activity?.handleMessage()
        }
    }

    private fun handleMessage() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val version = findViewById<MaterialTextView>(R.id.version)
        version.text = BuildConfig.VERSION_NAME

        progress = findViewById(R.id.progress)
        latest = findViewById(R.id.latest)
        update = findViewById(R.id.update)
        val sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val save = sharedPrefs.getBoolean("saver", false)
        if (save) {
            if (Tools.isWifi(applicationContext)) {
                networkTask()
            } else {
                progress.visibility = View.GONE
                latest.text = getString(R.string.wifi)
                latest.visibility = View.VISIBLE
                update.visibility = View.GONE
            }
        } else {
            if (Tools.isOnline(applicationContext)) {
                networkTask()
            } else {
                progress.visibility = View.GONE
                latest.text = getString(R.string.online)
                latest.visibility = View.VISIBLE
                update.visibility = View.GONE
            }
        }
        update.setOnClickListener {
            val goPlayStore = Intent(Intent.ACTION_VIEW)
            goPlayStore.data = Uri.parse("market://details?id=" + applicationContext.packageName)
            startActivity(goPlayStore)
        }

        val legal = findViewById<MaterialButton>(R.id.legal)
        legal.setOnClickListener {
            val bottomSheetFragment = LegalDialog()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        val license = findViewById<MaterialButton>(R.id.license)
        license.setOnClickListener {
            val intent = Intent(this, LicenseActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_about, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_information -> {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
                return true
            }
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun networkTask() {
        object : Thread() {
            override fun run() {
                try {
                    val doc = Jsoup.connect("https://github.com/gpillusion/CheckFirm/blob/master/VERSION").get()
                    val version = doc.select("#LC4")

                    for (el in version) {
                        latestVersion = Integer.parseInt(el.text())
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }

                mHandler.post {
                    progress.visibility = View.GONE
                    if (BuildConfig.VERSION_CODE >= latestVersion) {
                        latest.visibility = View.VISIBLE
                        update.visibility = View.GONE
                    } else {
                        latest.visibility = View.GONE
                        update.visibility = View.VISIBLE
                    }
                    mHandler.sendEmptyMessage(0)
                }
            }
        }.start()
    }
}