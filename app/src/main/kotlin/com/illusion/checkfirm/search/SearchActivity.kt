package com.illusion.checkfirm.search

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.illusion.checkfirm.R
import com.illusion.checkfirm.database.BookmarkDB
import com.illusion.checkfirm.database.BookmarkDBHelper
import com.illusion.checkfirm.database.HistoryDB
import com.illusion.checkfirm.database.HistoryDBHelper
import com.illusion.checkfirm.utils.Tools
import java.text.SimpleDateFormat
import java.util.*

class SearchActivity : AppCompatActivity() {

    private lateinit var sharedPrefs: SharedPreferences
    private val bookmarkList = ArrayList<BookmarkDB>()
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private val historyList = ArrayList<HistoryDB>()
    private lateinit var historyHelper: HistoryDBHelper
    private lateinit var model: EditText
    private lateinit var csc: EditText
    private lateinit var imm: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        model = findViewById(R.id.model)
        csc = findViewById(R.id.csc)
        model.setSelection(model.text.length)
        csc.setOnEditorActionListener { _, i, _ ->
            when (i) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    search()
                    true
                }
                else -> true
            }
        }

        if (Intent.ACTION_VIEW == intent.action) {
            //val url = intent.data!!
            //model.setText(url.getQueryParameter("model"))
            //csc.setText(url.getQueryParameter("csc"))
            //search()
        }

        val bookmarkChipGroup = findViewById<ChipGroup>(R.id.chipGroup)
        val bookmarkHelper = BookmarkDBHelper(this)
        bookmarkList.addAll(bookmarkHelper.allBookmarkDB)

        for (i in bookmarkList.indices) {
            val bookmarkChip = Chip(this)
            bookmarkChip.text = bookmarkList[i].name
            bookmarkChip.isCheckable = false
            bookmarkChip.setOnClickListener {
                model.setText(bookmarkList[i].model)
                csc.setText(bookmarkList[i].csc)
                search()
            }
            bookmarkChipGroup.addView(bookmarkChip)
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

        val quick = findViewById<MaterialCardView>(R.id.quick)
        if (historyList.isEmpty() && bookmarkList.isEmpty()) {
           quick.visibility = View.GONE
        } else if (historyList.isEmpty()) {
            historyRecyclerView.visibility = View.GONE
        } else if (bookmarkList.isEmpty()) {
            bookmarkChipGroup.visibility = View.GONE
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
        val modelString = model.text!!.trim().toString().toUpperCase()
        val cscString = csc.text!!.trim().toString().toUpperCase()
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


    private fun showKeyboard() {
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun hideKeyboard() {
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }
}