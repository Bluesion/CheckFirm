package com.illusion.checkfirm.search

import android.app.Activity.RESULT_OK
import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.card.MaterialCardView
import com.illusion.checkfirm.help.HelpActivity
import com.illusion.checkfirm.R
import com.illusion.checkfirm.dialogs.ContactDialog
import com.illusion.checkfirm.dialogs.SearchDialog
import com.illusion.checkfirm.settings.SettingsActivity
import com.illusion.checkfirm.utils.Tools
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.MalformedURLException
import java.net.URL
import java.util.ArrayList

class Search : Fragment() {

    private var baseURL = "https://fota-cloud-dn.ospserver.net/firmware/"
    private var officialURL = "/version.xml"
    private var testURL = "/version.test.xml"
    internal lateinit var latestOfficial: String
    internal lateinit var latestTest: String
    internal lateinit var officialFirmware: ArrayList<String>
    internal lateinit var testFirmware: ArrayList<String>
    internal lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    internal lateinit var modelOfficial: TextView
    internal lateinit var modelTest: TextView
    internal lateinit var latestOfficialFirmware: TextView
    internal lateinit var latestTestFirmware: TextView
    private lateinit var mResult: LinearLayout
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var welcomeCardView: MaterialCardView
    private var clipboard: ClipboardManager? = null
    private var clip: ClipData? = null
    private var previousOfficial = ""
    private var previousTest = ""
    private var model = ""
    private var csc = ""

    private val handler = MyHandler(this@Search)
    private class MyHandler (fragment: Search) : Handler() {
        private val mFragment: WeakReference<Search> = WeakReference(fragment)

        override fun handleMessage(msg: Message) {
            val fragment = mFragment.get()
            fragment?.handleMessage()
        }
    }

    private fun handleMessage() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_search, container, false)

        welcomeCardView = rootView.findViewById(R.id.welcome)
        val welcomeTitle = rootView.findViewById<TextView>(R.id.welcome_title)
        val welcomeText = rootView.findViewById<TextView>(R.id.welcome_text)
        sharedPrefs = activity!!.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val welcome = sharedPrefs.getBoolean("welcome", false)
        val save = sharedPrefs.getBoolean("saver", false)
        model = sharedPrefs.getString("welcome_model", "SM-A720S")!!
        csc = sharedPrefs.getString("welcome_csc", "SKC")!!
        if (welcome) {
            val official = "$baseURL$csc/$model$officialURL"
            val test = "$baseURL$csc/$model$testURL"
            if (save) {
                if (Tools.isWifi(activity!!)) {
                    networkTask(official, test)
                } else {
                    welcomeTitle.text = getString(R.string.wifi)
                    welcomeText.text = getString(R.string.welcome_wifi)
                    welcomeCardView.visibility = View.VISIBLE
                }
            } else {
                if (Tools.isOnline(activity!!)) {
                    networkTask(official, test)
                } else {
                    welcomeTitle.text = getString(R.string.online)
                    welcomeText.text = getString(R.string.welcome_online)
                    welcomeCardView.visibility = View.VISIBLE
                }
            }
        } else {
            welcomeTitle.text = getString(R.string.welcome_search)
            welcomeText.text = getString(R.string.welcome_disabled)
            welcomeCardView.visibility = View.VISIBLE
        }

        mResult = rootView.findViewById(R.id.result)

        mSwipeRefreshLayout = rootView.findViewById(R.id.mSwipeRefreshLayout)
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.green)
        mSwipeRefreshLayout.isEnabled = false

        modelOfficial = rootView.findViewById(R.id.model_official)
        modelTest = rootView.findViewById(R.id.model_test)
        latestOfficialFirmware = rootView.findViewById(R.id.latestOfficialFirmware)
        latestTestFirmware = rootView.findViewById(R.id.latestTestFirmware)

        clipboard = activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val infoOfficialFirmware = rootView.findViewById<MaterialCardView>(R.id.officialCardView)
        infoOfficialFirmware.setOnClickListener {
            val bottomSheetFragment = SearchDialog.newInstance(true, previousOfficial, model, csc)
            bottomSheetFragment.show(activity!!.supportFragmentManager, bottomSheetFragment.tag)
        }
        infoOfficialFirmware.setOnLongClickListener {
            clip = ClipData.newPlainText("checkfirmLatestOfficial", latestOfficial)
            clipboard?.primaryClip = clip!!
            Toast.makeText(activity!!, R.string.clipboard, Toast.LENGTH_SHORT).show()
            true
        }

        val infoTestFirmware = rootView.findViewById<MaterialCardView>(R.id.testCardView)
        infoTestFirmware.setOnClickListener {
            val bottomSheetFragment = SearchDialog.newInstance(false, previousTest, model, csc)
            bottomSheetFragment.show(activity!!.supportFragmentManager, bottomSheetFragment.tag)
        }
        infoTestFirmware.setOnLongClickListener {
            clip = ClipData.newPlainText("checkfirmLatestTest", latestTest)
            clipboard?.primaryClip = clip!!
            Toast.makeText(activity!!, R.string.clipboard, Toast.LENGTH_SHORT).show()
            true
        }

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.bookmark_help).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                val intent = Intent(activity!!, SearchActivity::class.java)
                startActivityForResult(intent, 1)
                return true
            }
            R.id.search_help -> {
                val intent = Intent(activity!!, HelpActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.settings -> {
                val intent = Intent(activity!!, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.contact -> {
                val bottomSheetFragment = ContactDialog()
                bottomSheetFragment.show(activity!!.supportFragmentManager, bottomSheetFragment.tag)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            model = data!!.getStringExtra("model")
            csc = data.getStringExtra("csc")
            val official = "$baseURL$csc/$model$officialURL"
            val test = "$baseURL$csc/$model$testURL"

            val save = sharedPrefs.getBoolean("saver", false)
            if (save) {
                if (Tools.isWifi(activity!!)) {
                    networkTask(official, test)
                } else {
                    Toast.makeText(activity!!, R.string.only_wifi, Toast.LENGTH_SHORT).show()
                }
            } else {
                if (Tools.isOnline(activity!!)) {
                    networkTask(official, test)
                } else {
                    Toast.makeText(activity!!, R.string.check_network, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun networkTask(officialURL: String, testURL: String) {
        officialFirmware = ArrayList()
        officialFirmware.clear()

        testFirmware = ArrayList()
        testFirmware.clear()

        latestOfficial = ""
        latestTest = ""

        val mHandler = MyHandler(this@Search)
        object : Thread() {
            override fun run() {
                mHandler.post {
                    mSwipeRefreshLayout.isEnabled = true
                    mSwipeRefreshLayout.isRefreshing = true
                }
                try {
                    val official = Jsoup.parse(URL(officialURL).openStream(), "UTF-8", "", Parser.xmlParser())
                    for (el in official.select("latest")) {
                        latestOfficial = el.text()
                    }
                    for (el in official.select("value")) {
                        val firmwares = el.text()
                        officialFirmware.add(firmwares)
                    }

                    val test = Jsoup.parse(URL(testURL).openStream(), "UTF-8", "", Parser.xmlParser())
                    for (el in test.select("latest")) {
                        latestTest = el.text()
                    }
                    for (el in test.select("value")) {
                        val firmwares = el.text()
                        testFirmware.add(firmwares)
                    }

                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                mHandler.post {
                    welcomeCardView.visibility = View.GONE
                    previousOfficial = officialFirmware.toString()
                            .replace(", ", "\n")
                            .replace("[", "")
                            .replace("]", "")
                    previousTest = testFirmware.toString()
                            .replace(", ", "\n")
                            .replace("[", "")
                            .replace("]", "")
                    modelOfficial.text = String.format(getString(R.string.device_format), model, csc)
                    latestOfficialFirmware.text = latestOfficial
                    modelTest.text = String.format(getString(R.string.device_format), model, csc)
                    latestTestFirmware.text = latestTest
                    mResult.visibility = View.VISIBLE
                    mSwipeRefreshLayout.isEnabled = false
                    mSwipeRefreshLayout.isRefreshing = false
                    handler.sendEmptyMessage(0)
                }
            }
        }.start()
    }
}
