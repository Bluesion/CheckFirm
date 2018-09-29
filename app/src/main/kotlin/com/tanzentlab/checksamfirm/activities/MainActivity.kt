package com.tanzentlab.checksamfirm.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.tanzentlab.checksamfirm.utils.ExceptionHandler
import com.tanzentlab.checksamfirm.R
import com.tanzentlab.checksamfirm.adapters.FastBookMarkAdapter
import com.tanzentlab.checksamfirm.adapters.MyExpandableAdapter
import com.tanzentlab.checksamfirm.database.BookMark
import com.tanzentlab.checksamfirm.database.DatabaseHelper
import com.tanzentlab.checksamfirm.utils.RecyclerTouchListener
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
    private lateinit var device: TextInputEditText
    internal lateinit var csc: TextInputEditText
    internal lateinit var mAdapter: MyExpandableAdapter
    internal lateinit var mAdapter2: MyExpandableAdapter
    internal lateinit var mOfficialListView: ExpandableListView
    internal lateinit var mTestListView: ExpandableListView
    private lateinit var mBookMarkAdapter: FastBookMarkAdapter
    private val mBookMarkList = ArrayList<BookMark>()
    private lateinit var mDB: DatabaseHelper
    private lateinit var officialHeader: ArrayList<String>
    private lateinit var testHeader: ArrayList<String>
    private lateinit var officialHashMap: HashMap<String, ArrayList<String>>
    private lateinit var testHashMap: HashMap<String, ArrayList<String>>
    private lateinit var mResult: LinearLayout

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

        mResult = findViewById(R.id.result)
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

        device.setupClearButtonWithAction()
        csc.setupClearButtonWithAction()

        officialHeader = ArrayList()
        testHeader = ArrayList()
        officialHeader.add(getString(R.string.previous_official))
        testHeader.add(getString(R.string.previous_test))

        val search = findViewById<MaterialButton>(R.id.search)
        search.setOnClickListener {
            if (isOnline(applicationContext)) {
                networkTask()
            } else {
                Toast.makeText(applicationContext, R.string.check_network, Toast.LENGTH_SHORT).show()
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
        mBookMarkList.clear()
        mBookMarkList.addAll(mDB.allBookMark)
        mBookMarkAdapter.notifyDataSetChanged()
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
                    officialHashMap = HashMap()
                    officialHashMap[officialHeader[0]] = officialFirmware
                    testHashMap = HashMap()
                    testHashMap[testHeader[0]] = testFirmware
                    mAdapter = MyExpandableAdapter(this@MainActivity, officialHeader, officialHashMap)
                    mAdapter2 = MyExpandableAdapter(this@MainActivity, testHeader, testHashMap)
                    mOfficialListView.setAdapter(mAdapter)
                    mTestListView.setAdapter(mAdapter2)
                    mOfficialListView.setOnGroupClickListener { parent, _, groupPosition, _ ->
                        setListViewHeight(parent, groupPosition)
                        false
                    }
                    mTestListView.setOnGroupClickListener { parent, _, groupPosition, _ ->
                        setListViewHeight(parent, groupPosition)
                        false
                    }
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

    private fun TextInputEditText.setupClearButtonWithAction() {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                val clearIcon = if (editable?.isNotEmpty() == true) R.drawable.ic_clear else 0
                setCompoundDrawablesWithIntrinsicBounds(0, 0, clearIcon, 0)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })

        setOnTouchListener(View.OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (this.right - this.compoundPaddingRight)) {
                    this.setText("")
                    return@OnTouchListener true
                }
            }
            return@OnTouchListener false
        })
    }

    private fun setListViewHeight(listView: ExpandableListView, group: Int) {
        val listAdapter = listView.expandableListAdapter as ExpandableListAdapter
        var totalHeight = 0
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.EXACTLY)
        for (i in 0 until listAdapter.groupCount) {
            val groupItem = listAdapter.getGroupView(i, false, null, listView)
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)

            totalHeight += groupItem.measuredHeight

            if (listView.isGroupExpanded(i) && i != group || !listView.isGroupExpanded(i) && i == group) {
                for (j in 0 until listAdapter.getChildrenCount(i)) {
                    val listItem = listAdapter.getChildView(i, j, false, null, listView)
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
                    totalHeight += listItem.measuredHeight
                }
            }
        }

        val params = listView.layoutParams
        var height = totalHeight + listView.dividerHeight * (listAdapter.groupCount - 1)
        if (height < 10)
            height = 200
        params.height = height
        listView.layoutParams = params
        listView.requestLayout()
    }

    companion object {
        fun isOnline(mContext: Context): Boolean {
            val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE)
            return if (connectivityManager is ConnectivityManager) {
                val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected ?: false
            } else false
        }
    }
}