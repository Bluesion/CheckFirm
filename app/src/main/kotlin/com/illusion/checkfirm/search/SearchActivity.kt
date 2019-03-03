package com.illusion.checkfirm.search

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.illusion.checkfirm.R
import com.illusion.checkfirm.database.BookmarkDB
import com.illusion.checkfirm.database.BookmarkDBHelper
import com.illusion.checkfirm.database.HistoryDB
import com.illusion.checkfirm.database.HistoryDBHelper
import com.illusion.checkfirm.utils.RecyclerTouchListener
import com.illusion.checkfirm.utils.ThemeChanger
import com.illusion.checkfirm.utils.Tools
import java.text.SimpleDateFormat
import java.util.*

class SearchActivity : AppCompatActivity() {

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var bookmarkRecyclerView: RecyclerView
    private lateinit var bookmarkAdapter: FastBookmarkAdapter
    private val bookmarkList = ArrayList<BookmarkDB>()
    private lateinit var bookmarkHelper: BookmarkDBHelper
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private val historyList = ArrayList<HistoryDB>()
    private lateinit var historyHelper: HistoryDBHelper
    private lateinit var model: EditText
    private lateinit var csc: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeChanger.setAppTheme(this)
        setContentView(R.layout.activity_search)

        sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val one = sharedPrefs.getBoolean("one", true)
        val mAppBar = findViewById<AppBarLayout>(R.id.appbar)
        val height = (resources.displayMetrics.heightPixels * 0.3976)
        val lp = mAppBar.layoutParams
        lp.height = height.toInt()
        if (one) {
            mAppBar.setExpanded(true)
        } else {
            mAppBar.setExpanded(false)
        }

        val expandedTitle = findViewById<TextView>(R.id.expanded_title)
        mAppBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, _ ->
            val percentage = (appBarLayout.y / appBarLayout.totalScrollRange)
            expandedTitle.alpha = 1 - (percentage * 2 * -1)
        })

        model = findViewById(R.id.model)
        csc = findViewById(R.id.csc)
        val modelFilters = model.filters
        val newModelFilters = arrayOfNulls<InputFilter>(modelFilters.size + 1)
        System.arraycopy(modelFilters, 0, newModelFilters, 0, modelFilters.size)
        newModelFilters[modelFilters.size] = InputFilter.AllCaps()
        model.filters = newModelFilters

        val cscFilters = csc.filters
        val newCSCFilters = arrayOfNulls<InputFilter>(cscFilters.size + 1)
        System.arraycopy(cscFilters, 0, newCSCFilters, 0, cscFilters.size)
        newCSCFilters[cscFilters.size] = InputFilter.AllCaps()
        csc.filters = newCSCFilters

        bookmarkRecyclerView = findViewById(R.id.bookmarkRecyclerView)
        bookmarkAdapter = FastBookmarkAdapter(bookmarkList)
        val bookmarkLayout = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        bookmarkRecyclerView.layoutManager = bookmarkLayout
        bookmarkRecyclerView.itemAnimator = DefaultItemAnimator()
        bookmarkRecyclerView.adapter = bookmarkAdapter
        bookmarkRecyclerView.addOnItemTouchListener(RecyclerTouchListener(this, bookmarkRecyclerView, object: RecyclerTouchListener.ClickListener {
            override fun onClick(view: View, position: Int) {
                if (position != RecyclerView.NO_POSITION) {
                    model.setText(bookmarkList[position].model)
                    csc.setText(bookmarkList[position].csc)
                    search()
                }
            }

            override fun onLongClick(view: View?, position: Int) {}
        }))

        bookmarkHelper = BookmarkDBHelper(this)
        bookmarkList.addAll(bookmarkHelper.allBookmarkDB)
        if (bookmarkList.isEmpty()) {
            bookmarkRecyclerView.visibility = View.GONE
        } else {
            bookmarkRecyclerView.visibility = View.VISIBLE
        }

        historyRecyclerView = findViewById(R.id.historyRecyclerView)
        historyAdapter = HistoryAdapter(historyList, object : HistoryAdapter.MyAdapterListener {
            override fun onItemClicked(v: View, position: Int) {
                model.setText(historyList[position].model)
                csc.setText(historyList[position].csc)
                search()
            }

            override fun onDeleteClicked(v: View, position: Int) {
                historyHelper.deleteHistory(historyList[position])
                historyList.removeAt(position)
                historyList.clear()
                historyList.addAll(historyHelper.allHistoryDB)
                historyAdapter.notifyDataSetChanged()
            }
        })
        val historyLayout = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        historyRecyclerView.layoutManager = historyLayout
        historyRecyclerView.itemAnimator = DefaultItemAnimator()
        historyRecyclerView.adapter = historyAdapter

        historyHelper = HistoryDBHelper(this)
        historyList.addAll(historyHelper.allHistoryDB)
        if (historyList.isEmpty()) {
            historyRecyclerView.visibility = View.GONE
        } else {
            historyRecyclerView.visibility = View.VISIBLE
        }

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            hideKeyboard()
            finish()
        }

        val search = findViewById<ImageView>(R.id.search)
        search.setOnClickListener {
            search()
        }
    }

    private fun createHistory(model: String, csc: String) {
        val today = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN)
        val date = formatter.format(today)
        val device = "$model$csc"
        val id = historyHelper.insertHistory(model, csc, device, date)

        val n = historyHelper.getHistory(id)
        historyList.add(n)
    }

    private fun search() {
        hideKeyboard()
        val modelString = model.text!!.toString()
        val cscString = csc.text!!.toString()
        val sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val save = sharedPrefs.getBoolean("saver", false)
        if (save) {
            if (Tools.isWifi(applicationContext)) {
                createHistory(modelString, cscString)
                intent.putExtra("model", modelString)
                intent.putExtra("csc", cscString)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(applicationContext, R.string.only_wifi, Toast.LENGTH_SHORT).show()
            }
        } else {
            if (Tools.isOnline(applicationContext)) {
                createHistory(modelString, cscString)
                intent.putExtra("model", modelString)
                intent.putExtra("csc", cscString)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(applicationContext, R.string.check_network, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(findViewById<View>(android.R.id.content).windowToken, 0)
    }
}