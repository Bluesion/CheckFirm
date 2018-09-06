package com.illusion.checkfirm.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.text.InputFilter
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.illusion.checkfirm.utils.ExceptionHandler
import com.illusion.checkfirm.adapters.ListAdapter
import com.illusion.checkfirm.R
import com.illusion.checkfirm.adapters.FastBookMarkAdapter
import com.illusion.checkfirm.database.BookMark
import com.illusion.checkfirm.database.DatabaseHelper
import com.illusion.checkfirm.utils.RecyclerTouchListener
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import java.io.IOException
import java.lang.ref.WeakReference
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
    private lateinit var device: AppCompatEditText
    internal lateinit var csc: AppCompatEditText
    internal lateinit var mAdapter: ListAdapter
    internal lateinit var mAdapter2: ListAdapter
    internal lateinit var mOfficialListView: ListView
    internal lateinit var mTestListView: ListView
    private lateinit var mBookMarkAdapter: FastBookMarkAdapter
    private val mBookMarkList = ArrayList<BookMark>()
    private lateinit var mDB: DatabaseHelper

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
        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        mOfficialListView = findViewById(R.id.mOfficialListView)
        mTestListView = findViewById(R.id.mTestListView)
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
                    networkTask()
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bookmark, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bookmark -> {
                val intent = Intent(this, BookMarkActivity::class.java)
                this.startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    public override fun onRestart() {
        super.onRestart()
        finish()
        startActivity(intent)
    }

    private fun networkTask() {
        officialFinalURL = baseURL + csc.text!!.toString() + "/" + device.text!!.toString() + officialURL
        testFinalURL = baseURL + csc.text!!.toString() + "/" + device.text!!.toString() + testURL

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

                } catch (e: IOException) {}

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
            val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE)
            return if (connectivityManager is ConnectivityManager) {
                val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected ?: false
            } else false
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