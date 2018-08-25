package com.illusion.checkfirm

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.Toolbar
import android.text.InputFilter
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.URL
import java.util.ArrayList
import java.util.Objects

class MainActivity : AppCompatActivity() {

    private lateinit var cc: String
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
    private lateinit var device: AppCompatEditText
    internal lateinit var csc: AppCompatEditText
    internal lateinit var mAdapter: ListAdapter
    internal lateinit var mAdapter2: ListAdapter
    internal lateinit var mOfficialListView: ListView
    internal lateinit var mTestListView: ListView

    private val handler = MyHandler(this@MainActivity)

    private class MyHandler (activity: MainActivity) : Handler() {
        private val mActivity: WeakReference<MainActivity> = WeakReference(activity)

        override fun handleMessage(msg: Message) {
            val activity = mActivity.get()
            activity?.handleMessage()
        }
    }

    private fun handleMessage() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        mOfficialListView = findViewById(R.id.mOfficialListView)
        mTestListView = findViewById(R.id.mTestListView)
        device = findViewById(R.id.model)
        csc = findViewById(R.id.csc)
        val editFilters = device.filters
        val newFilters = arrayOfNulls<InputFilter>(editFilters.size + 1)
        System.arraycopy(editFilters, 0, newFilters, 0, editFilters.size)
        newFilters[editFilters.size] = InputFilter.AllCaps()
        device.filters = newFilters
        csc.filters = newFilters

        val search = findViewById<AppCompatButton>(R.id.search)
        search.setOnClickListener {
            if (isOnline(applicationContext)) {
                networkTask()
            } else {
                Toast.makeText(applicationContext, "네트워크에 연결해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        mSwipeRefreshLayout = findViewById(R.id.mSwipeRefreshLayout)
        mSwipeRefreshLayout.setColorSchemeResources(R.color.appColor)
        mSwipeRefreshLayout.isEnabled = false

        latestOfficialFirmware = findViewById(R.id.latestOfficialFirmware)
        latestTestFirmware = findViewById(R.id.latestTestFirmware)
    }

    private fun networkTask() {
        val name = device.text.toString()
        val cc = csc.text.toString()
        this.cc = if (cc.isEmpty()) {
            when {
                name.endsWith("S") -> "SKC"
                name.endsWith("K") -> "KTC"
                name.endsWith("L") -> "LUC"
                else -> "KOO"
            }
        } else {
            cc
        }

        officialFinalURL = baseURL + this.cc + "/" + device.text!!.toString() + officialURL
        testFinalURL = baseURL + this.cc + "/" + device.text!!.toString() + testURL

        val mHandler = MyHandler(this@MainActivity)
        object : Thread() {
            override fun run() {
                mHandler.post {
                    mSwipeRefreshLayout.isEnabled = true
                    mSwipeRefreshLayout.isRefreshing = true
                }
                try {
                    officialFirmware = ArrayList()
                    val official = Jsoup.parse(URL(officialFinalURL).openStream(), "UTF-8", "", Parser.xmlParser())

                    for (el in official.select("latest")) {
                        latestOfficial = el.text()
                    }

                    for (el in official.select("value")) {
                        val firmwares = el.text()
                        officialFirmware.add(firmwares)
                    }

                    testFirmware = ArrayList()
                    val test = Jsoup.parse(URL(testFinalURL).openStream(), "UTF-8", "", Parser.xmlParser())

                    for (el in test.select("latest")) {
                        latestTest = el.text()
                    }

                    for (el in test.select("value")) {
                        val firmwares = el.text()
                        testFirmware.add(firmwares)
                    }

                } catch (e: IOException) {
                    val toast = Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT)
                    toast.show()
                }

                mHandler.post {
                    mAdapter = ListAdapter(this@MainActivity, officialFirmware)
                    mAdapter2 = ListAdapter(this@MainActivity, testFirmware)
                    mOfficialListView.adapter = mAdapter
                    mTestListView.adapter = mAdapter2
                    setListViewHeightBasedOnChildren(mOfficialListView)
                    setListViewHeightBasedOnChildren(mTestListView)
                    latestOfficialFirmware.text = latestOfficial
                    latestTestFirmware.text = latestTest
                    mSwipeRefreshLayout.isEnabled = false
                    mSwipeRefreshLayout.isRefreshing = false
                    handler.sendEmptyMessage(0)
                }
            }
        }.start()
    }

    companion object {
        fun isOnline(mContext: Context): Boolean {
            val manager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mobile = Objects.requireNonNull(manager).getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            val wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)

            return wifi.isConnected || mobile.isConnected
        }

        fun setListViewHeightBasedOnChildren(listView: ListView) {
            val listAdapter = listView.adapter ?: return

            var totalHeight = 0
            val desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.AT_MOST)

            for (i in 0 until listAdapter.count) {
                val listItem = listAdapter.getView(i, null, listView)
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
                totalHeight += listItem.measuredHeight
            }

            val params = listView.layoutParams
            params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
            listView.layoutParams = params
            listView.requestLayout()
        }
    }
}