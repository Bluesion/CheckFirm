package com.illusion.checkfirm.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.InputFilter
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.illusion.checkfirm.R
import com.illusion.checkfirm.adapters.FastBookMarkAdapter
import com.illusion.checkfirm.database.BookMark
import com.illusion.checkfirm.database.DatabaseHelper
import com.illusion.checkfirm.utils.RecyclerTouchListener
import com.illusion.checkfirm.utils.ThemeChanger
import com.illusion.checkfirm.utils.Tools
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.MalformedURLException
import java.net.URL
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    internal lateinit var officialFinalURL: String
    private lateinit var testFinalURL: String
    internal lateinit var latestOfficial: String
    internal lateinit var latestTest: String
    private var baseURL = "https://fota-cloud-dn.ospserver.net/firmware/"
    private var officialURL = "/version.xml"
    private var testURL = "/version.test.xml"
    internal lateinit var officialFirmware: ArrayList<String>
    internal lateinit var testFirmware: ArrayList<String>
    internal lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    internal lateinit var latestOfficialFirmware: TextView
    internal lateinit var latestTestFirmware: TextView
    private lateinit var device: TextInputEditText
    internal lateinit var csc: TextInputEditText
    private lateinit var mBookMarkAdapter: FastBookMarkAdapter
    private val mBookMarkList = ArrayList<BookMark>()
    private lateinit var mDB: DatabaseHelper
    private lateinit var mResult: LinearLayout
    private var previousOfficial = ""
    private var previousTest = ""

    private val handler = MyHandler(this@MainActivity)
    private class MyHandler (activity: MainActivity): Handler() {
        private val mActivity: WeakReference<MainActivity> = WeakReference(activity)

        override fun handleMessage(msg: Message) {
            val activity = mActivity.get()
            activity?.handleMessage()
        }
    }

    private fun handleMessage() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeChanger.setAppTheme(this)
        setContentView(R.layout.activity_main)

        val sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val one = sharedPrefs.getBoolean("one", true)
        val mAppBar = findViewById<AppBarLayout>(R.id.appbar)
        if (!one) {
            mAppBar.setExpanded(true)
        } else {
            mAppBar.setExpanded(false)
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        mResult = findViewById(R.id.result)
        device = findViewById(R.id.model)
        csc = findViewById(R.id.csc)

        val mIntent: Intent = intent
        if (mIntent.getStringExtra("model") != null) {
            device.setText(mIntent.getStringExtra("model"))
        }
        if (mIntent.getStringExtra("csc") != null) {
            csc.setText(mIntent.getStringExtra("csc"))
        }

        val deviceFilters = device.filters
        val newDeviceFilters = arrayOfNulls<InputFilter>(deviceFilters.size + 1)
        System.arraycopy(deviceFilters, 0, newDeviceFilters, 0, deviceFilters.size)
        newDeviceFilters[deviceFilters.size] = InputFilter.AllCaps()
        device.filters = newDeviceFilters

        val cscFilters = csc.filters
        val newCSCFilters = arrayOfNulls<InputFilter>(cscFilters.size + 1)
        System.arraycopy(cscFilters, 0, newCSCFilters, 0, cscFilters.size)
        newCSCFilters[cscFilters.size] = InputFilter.AllCaps()
        csc.filters = newCSCFilters

        val save = sharedPrefs.getBoolean("saver", true)
        val search = findViewById<MaterialButton>(R.id.search)
        search.setOnClickListener {
            if (!save) {
                if (Tools.isWifi(applicationContext)) {
                    if (Tools.isOnline(applicationContext)) {
                        networkTask()
                    } else {
                        Toast.makeText(applicationContext, R.string.check_network, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, R.string.only_wifi, Toast.LENGTH_SHORT).show()
                }
            } else {
                if (Tools.isOnline(applicationContext)) {
                    networkTask()
                } else {
                    Toast.makeText(applicationContext, R.string.check_network, Toast.LENGTH_SHORT).show()
                }
            }
        }

        mSwipeRefreshLayout = findViewById(R.id.mSwipeRefreshLayout)
        mSwipeRefreshLayout.setColorSchemeResources(R.color.appColor)
        mSwipeRefreshLayout.isEnabled = false

        latestOfficialFirmware = findViewById(R.id.latestOfficialFirmware)
        latestTestFirmware = findViewById(R.id.latestTestFirmware)

        val mRecyclerView = findViewById<RecyclerView>(R.id.mRecyclerView)
        mBookMarkAdapter = FastBookMarkAdapter(mBookMarkList)
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mRecyclerView.adapter = mBookMarkAdapter
        mRecyclerView.addOnItemTouchListener(RecyclerTouchListener(this, mRecyclerView, object: RecyclerTouchListener.ClickListener {
            override fun onClick(view: View, position: Int) {
                if (position != RecyclerView.NO_POSITION) {
                    device.setText(mBookMarkList[position].model)
                    csc.setText(mBookMarkList[position].csc)
                    if (!save) {
                        if (Tools.isWifi(applicationContext)) {
                            if (Tools.isOnline(applicationContext)) {
                                networkTask()
                            } else {
                                Toast.makeText(applicationContext, R.string.check_network, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(applicationContext, R.string.only_wifi, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        if (Tools.isOnline(applicationContext)) {
                            networkTask()
                        } else {
                            Toast.makeText(applicationContext, R.string.check_network, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onLongClick(view: View?, position: Int) {
                val intent = Intent(this@MainActivity, BookMarkActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                this@MainActivity.startActivity(intent)
            }
        }))

        mDB = DatabaseHelper(this)
        mBookMarkList.addAll(mDB.allBookMark)

        val previousOfficialAlertDialog = findViewById<LinearLayout>(R.id.previousOfficial)
        previousOfficialAlertDialog.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.previous_official)
            builder.setMessage(previousOfficial)
            builder.setPositiveButton(android.R.string.ok, null)
            builder.show()
        }

        val previousTestAlertDialog = findViewById<LinearLayout>(R.id.previousTest)
        previousTestAlertDialog.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.previous_test)
            builder.setMessage(previousTest)
            builder.setPositiveButton(android.R.string.ok, null)
            builder.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bookmark -> {
                val intent = Intent(this, BookMarkActivity::class.java)
                this.startActivity(intent)
                return true
            }
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                this.startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    public override fun onRestart() {
        super.onRestart()
        mBookMarkList.clear()
        mBookMarkList.addAll(mDB.allBookMark)
        mBookMarkAdapter.notifyDataSetChanged()
    }

    private fun networkTask() {
        officialFirmware = ArrayList()
        officialFirmware.clear()

        testFirmware = ArrayList()
        testFirmware.clear()

        latestOfficial = ""
        latestTest = ""

        val cscString = csc.text!!.toString()
        val deviceString = device.text!!.toString()

        if (cscString.isBlank() || deviceString.isBlank()) {
            Toast.makeText(applicationContext, R.string.check_input, Toast.LENGTH_SHORT).show()
        } else {
            officialFinalURL = "$baseURL$cscString/$deviceString$officialURL"
            testFinalURL = "$baseURL$cscString/$deviceString$testURL"

            val mHandler = MyHandler(this@MainActivity)
            object : Thread() {
                override fun run() {
                    mHandler.post {
                        mSwipeRefreshLayout.isEnabled = true
                        mSwipeRefreshLayout.isRefreshing = true
                    }
                    try {
                        val official = Jsoup.parse(URL(officialFinalURL).openStream(), "UTF-8", "", Parser.xmlParser())
                        for (el in official.select("latest")) {
                            latestOfficial = el.text()
                        }
                        for (el in official.select("value")) {
                            val firmwares = el.text()
                            officialFirmware.add(firmwares)
                        }

                        val test = Jsoup.parse(URL(testFinalURL).openStream(), "UTF-8", "", Parser.xmlParser())
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
                        previousOfficial = officialFirmware.toString()
                                .replace(", ", "\n")
                                .replace("[", "")
                                .replace("]", "")
                        previousTest = testFirmware.toString()
                                .replace(", ", "\n")
                                .replace("[", "")
                                .replace("]", "")
                        mResult.visibility = View.VISIBLE
                        latestOfficialFirmware.text = latestOfficial
                        latestTestFirmware.text = latestTest
                        mSwipeRefreshLayout.isEnabled = false
                        mSwipeRefreshLayout.isRefreshing = false
                        handler.sendEmptyMessage(0)
                    }
                }
            }.start()
        }
    }
}