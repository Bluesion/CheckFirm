package com.illusion.checkfirm.search

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.MaterialToolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textview.MaterialTextView
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.database.bookmark.BookmarkViewModel
import com.illusion.checkfirm.primitive.HistoryItem
import com.illusion.checkfirm.primitive.SearchItem
import com.illusion.checkfirm.utils.Tools
import java.text.SimpleDateFormat
import java.util.*

class SearchActivity : AppCompatActivity() {

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var historyPrefs: SharedPreferences
    private lateinit var historyEditor: SharedPreferences.Editor
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var searchAdapter: SearchAdapter
    private val searchList = ArrayList<SearchItem>()
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private val historyList = ArrayList<HistoryItem>()
    private lateinit var model: EditText
    private lateinit var csc: EditText
    private lateinit var imm: InputMethodManager
    private lateinit var viewModel: BookmarkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        historyPrefs = getSharedPreferences("search_history", Context.MODE_PRIVATE)
        historyEditor = historyPrefs.edit()
        sharedPrefs.edit().putString("search_mode", "single").apply()
        imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        initToolbar()

        // MULTI SEARCH
        val multiLayout = findViewById<MaterialCardView>(R.id.multi_layout)
        val searchLists = findViewById<MaterialTextView>(R.id.search_lists)
        searchLists.text = String.format(getString(R.string.multi_search_lists), 0)
        searchRecyclerView = findViewById(R.id.search_recyclerview)
        searchAdapter = SearchAdapter(this, searchList, object : SearchAdapter.MyAdapterListener {
            override fun onDeleteClicked(position: Int) {
                searchList.removeAt(position)
                searchAdapter.notifyDataSetChanged()
                searchLists.text = String.format(getString(R.string.multi_search_lists), searchList.size)
                if (searchList.isEmpty()) {
                    multiLayout.visibility = View.GONE
                } else {
                    multiLayout.visibility = View.VISIBLE
                }
            }
        })
        val searchLayout = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        searchRecyclerView.layoutManager = searchLayout
        searchRecyclerView.itemAnimator = DefaultItemAnimator()
        searchRecyclerView.adapter = searchAdapter

        val addButton = findViewById<AppCompatImageView>(R.id.add)
        addButton.setOnClickListener {
            if (searchList.size >= 10) {
                Toast.makeText(this, getString(R.string.multi_search_limit), Toast.LENGTH_SHORT).show()
            } else {
                val modelString = model.text!!.trim().toString().toUpperCase(Locale.US)
                val cscString = csc.text!!.trim().toString().toUpperCase(Locale.US)
                if (modelString.isBlank() || cscString.isBlank()) {
                    Toast.makeText(this, R.string.info_catcher_error, Toast.LENGTH_SHORT).show()
                } else {
                    val item = SearchItem(modelString, cscString)
                    addToSearchList(item)
                    searchAdapter.notifyDataSetChanged()
                    searchLists.text = String.format(getString(R.string.multi_search_lists), searchList.size)
                    multiLayout.visibility = View.VISIBLE
                }
            }
        }
        val multiSearchButton = findViewById<MaterialButton>(R.id.search_button)
        multiSearchButton.setOnClickListener {
            search()
        }

        // COMMON
        model = findViewById(R.id.model)
        csc = findViewById(R.id.csc)
        model.setSelection(model.text!!.length)
        csc.setOnEditorActionListener { _, i, _ ->
            when (i) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val modelString = model.text!!.trim().toString().toUpperCase(Locale.US)
                    val cscString = csc.text!!.trim().toString().toUpperCase(Locale.US)
                    if (modelString.isBlank() || cscString.isBlank()) {
                        Toast.makeText(this, R.string.info_catcher_error, Toast.LENGTH_SHORT).show()
                    } else {
                        val item = SearchItem(modelString, cscString)
                        if (sharedPrefs.getString("search_mode", "single") == "single") {
                            searchList.add(item)
                            search()
                        } else {
                            if (searchList.size >= 10) {
                                Toast.makeText(this, getString(R.string.multi_search_limit), Toast.LENGTH_SHORT).show()
                            } else {
                                addToSearchList(item)
                                searchAdapter.notifyDataSetChanged()
                                searchLists.text = String.format(getString(R.string.multi_search_lists), searchList.size)
                                multiLayout.visibility = View.VISIBLE
                            }
                        }
                    }
                    true
                }
                else -> true
            }
        }

        val quick = findViewById<MaterialCardView>(R.id.quick)
        val bookmarkChipGroup = findViewById<ChipGroup>(R.id.chip_group)
        viewModel = ViewModelProvider(this, CheckFirm.viewModelFactory).get(BookmarkViewModel::class.java)
        viewModel.allBookmarks.observe(this, androidx.lifecycle.Observer {
            if (it.isEmpty()) {
                if (historyList.isEmpty()) {
                    quick.visibility = View.GONE
                } else {
                    bookmarkChipGroup.visibility = View.GONE
                }
            } else {
                if (historyList.isEmpty()) {
                    historyRecyclerView.visibility = View.GONE
                }

                for (element in it) {
                    val bookmarkChip = Chip(this)
                    bookmarkChip.text = element.name
                    bookmarkChip.isCheckable = false
                    bookmarkChip.setOnClickListener {
                        model.setText(element.model)
                        csc.setText(element.csc)
                        val modelString = model.text!!.trim().toString().toUpperCase(Locale.US)
                        val cscString = csc.text!!.trim().toString().toUpperCase(Locale.US)
                        if (modelString.isBlank() || cscString.isBlank()) {
                            if (sharedPrefs.getString("search_mode", "single") == "single") {
                                Toast.makeText(this, R.string.info_catcher_error, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val item = SearchItem(modelString, cscString)
                            if (sharedPrefs.getString("search_mode", "single") == "single") {
                                searchList.add(item)
                                search()
                            } else {
                                if (searchList.size >= 10) {
                                    Toast.makeText(this, getString(R.string.multi_search_limit), Toast.LENGTH_SHORT).show()
                                } else {
                                    addToSearchList(item)
                                    searchAdapter.notifyDataSetChanged()
                                    searchLists.text = String.format(getString(R.string.multi_search_lists), searchList.size)
                                    multiLayout.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                    bookmarkChipGroup.addView(bookmarkChip)
                }
            }
        })

        historyRecyclerView = findViewById(R.id.history_recyclerview)
        historyAdapter = HistoryAdapter(historyList, object : HistoryAdapter.MyAdapterListener {
            override fun onItemClicked(position: Int) {
                model.setText(historyList[position].model)
                csc.setText(historyList[position].csc)
                val modelString = model.text!!.trim().toString().toUpperCase(Locale.US)
                val cscString = csc.text!!.trim().toString().toUpperCase(Locale.US)
                if (modelString.isBlank() || cscString.isBlank()) {
                    Toast.makeText(this@SearchActivity, R.string.info_catcher_error, Toast.LENGTH_SHORT).show()
                } else {
                    val item = SearchItem(modelString, cscString)
                    if (sharedPrefs.getString("search_mode", "single") == "single") {
                        searchList.add(item)
                        search()
                    } else {
                        if (searchList.size >= 10) {
                            Toast.makeText(this@SearchActivity, getString(R.string.multi_search_limit), Toast.LENGTH_SHORT).show()
                        } else {
                            addToSearchList(item)
                            searchAdapter.notifyDataSetChanged()
                            searchLists.text = String.format(getString(R.string.multi_search_lists), searchList.size)
                            multiLayout.visibility = View.VISIBLE
                        }
                    }
                }
            }

            override fun onDeleteClicked(position: Int) {
                historyList.removeAt(position)

                for (i in 0..9) {
                    historyEditor.putString("search_history_model_$i", "")
                    historyEditor.putString("search_history_csc_$i", "")
                    historyEditor.putString("search_history_date_$i", "")
                }

                for (i in 0 until historyList.size) {
                    historyEditor.putString("search_history_model_$i", historyList[i].model)
                    historyEditor.putString("search_history_csc_$i", historyList[i].csc)
                    historyEditor.putString("search_history_date_$i", historyList[i].date)
                }

                historyEditor.apply()
                historyAdapter.notifyDataSetChanged()
            }
        })

        val historyLayout = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        historyRecyclerView.layoutManager = historyLayout
        historyRecyclerView.adapter = historyAdapter

        for (i in 0..9) {
            val model = historyPrefs.getString("search_history_model_$i", "")!!
            val csc = historyPrefs.getString("search_history_csc_$i", "")!!
            val date = historyPrefs.getString("search_history_date_$i", "")!!

            if (model.isNotBlank() && csc.isNotBlank()) {
                val item = HistoryItem(model, csc, date)
                historyList.add(item)
            }
        }

        // SINGLE SEARCH
        val searchButton = findViewById<AppCompatImageView>(R.id.search)
        searchButton.setOnClickListener {
            val modelString = model.text!!.trim().toString().toUpperCase(Locale.US)
            val cscString = csc.text!!.trim().toString().toUpperCase(Locale.US)
            if (modelString.isBlank() || cscString.isBlank()) {
                Toast.makeText(this, R.string.info_catcher_error, Toast.LENGTH_SHORT).show()
            } else {
                val item = SearchItem(modelString, cscString)
                searchList.add(item)
                search()
            }
        }

        val tab1 = findViewById<MaterialButton>(R.id.single_search)
        val tab2 = findViewById<MaterialButton>(R.id.multi_search)
        tab1.setTextAppearance(R.style.SearchButton_Selected)
        tab2.setTextAppearance(R.style.SearchButton_Unselected)

        tab1.setOnClickListener {
            tab1.setTextAppearance(R.style.SearchButton_Selected)
            tab2.setTextAppearance(R.style.SearchButton_Unselected)
            sharedPrefs.edit().putString("search_mode", "single").apply()

            searchButton.visibility = View.VISIBLE
            addButton.visibility = View.GONE
            multiLayout.visibility = View.GONE
        }

        tab2.setOnClickListener {
            tab1.setTextAppearance(R.style.SearchButton_Unselected)
            tab2.setTextAppearance(R.style.SearchButton_Selected)
            sharedPrefs.edit().putString("search_mode", "multi").apply()

            searchButton.visibility = View.GONE
            addButton.visibility = View.VISIBLE
            if (searchList.isEmpty()) {
                multiLayout.visibility = View.GONE
            } else {
                multiLayout.visibility = View.VISIBLE
            }
        }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createHistory(model: String, csc: String) {
        var duplicated = false
        var duplicateIndex = 0
        val today = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN)
        val date = formatter.format(today)

        for (i in 0..9) {
            val tempModel = historyPrefs.getString("search_history_model_$i", "")
            val tempCsc = historyPrefs.getString("search_history_csc_$i", "")
            if (model == tempModel && csc == tempCsc) {
                duplicateIndex = i
                duplicated = true
            }
        }

        if (!duplicated) {
            if (historyList.size >= 10) {
                for (i in 9 downTo 0) {
                    val index = i + 1
                    historyEditor.putString("search_history_model_$index", historyPrefs.getString("search_history_model_$i", ""))
                    historyEditor.putString("search_history_csc_$index", historyPrefs.getString("search_history_csc_$i", ""))
                    historyEditor.putString("search_history_date_$index", historyPrefs.getString("search_history_date_$i", ""))
                }
                historyEditor.putString("search_history_model_0", model)
                historyEditor.putString("search_history_csc_0", csc)
                historyEditor.putString("search_history_date_0", date)
            } else {
                for (i in historyList.size - 1 downTo 0) {
                    val index = i + 1
                    historyEditor.putString("search_history_model_$index", historyPrefs.getString("search_history_model_$i", ""))
                    historyEditor.putString("search_history_csc_$index", historyPrefs.getString("search_history_csc_$i", ""))
                    historyEditor.putString("search_history_date_$index", historyPrefs.getString("search_history_date_$i", ""))
                }
                historyEditor.putString("search_history_model_0", model)
                historyEditor.putString("search_history_csc_0", csc)
                historyEditor.putString("search_history_date_0", date)
            }
        } else {
            if (duplicateIndex != 0) {
                val tempModel = historyPrefs.getString("search_history_model_$duplicateIndex", "")!!
                val tempCsc = historyPrefs.getString("search_history_csc_$duplicateIndex", "")!!
                for (i in 0 until duplicateIndex) {
                    val index = i + 1
                    historyEditor.putString("search_history_model_$index", historyPrefs.getString("search_history_model_$i", ""))
                    historyEditor.putString("search_history_csc_$index", historyPrefs.getString("search_history_csc_$i", ""))
                    historyEditor.putString("search_history_date_$index", historyPrefs.getString("search_history_date_$i", ""))
                }
                historyEditor.putString("search_history_model_0", tempModel)
                historyEditor.putString("search_history_csc_0", tempCsc)
            }
            historyEditor.putString("search_history_date_0", date)
        }
        historyEditor.apply()
    }

    private fun search() {
        hideKeyboard()

        var modelString = ""
        var cscString = ""
        for (element in searchList) {
            createHistory(element.model, element.csc)
            modelString += element.model + "%"
            cscString += element.csc + "%"
        }

        if (Tools.isOnline(this)) {
            intent.putExtra("model", modelString)
            intent.putExtra("csc", cscString)
            intent.putExtra("total", searchList.size)
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            Toast.makeText(this, R.string.check_network, Toast.LENGTH_SHORT).show()
        }
    }


    private fun showKeyboard() {
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun hideKeyboard() {
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    private fun initToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val toolbarText = getString(R.string.search)
        val title = findViewById<MaterialTextView>(R.id.title)
        title.text = toolbarText
        val expandedTitle = findViewById<MaterialTextView>(R.id.expanded_title)
        expandedTitle.text = toolbarText

        val mAppBar = findViewById<AppBarLayout>(R.id.appbar)
        mAppBar.layoutParams.height = (resources.displayMetrics.heightPixels * 0.3976).toInt()
        mAppBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, _ ->
            val percentage = (appBarLayout.y / appBarLayout.totalScrollRange)
            expandedTitle.alpha = 1 - (percentage * 2 * -1)
            title.alpha = percentage * -1
        })

        val one = sharedPrefs.getBoolean("one", true)
        if (one) {
            mAppBar.setExpanded(true)
        } else {
            mAppBar.setExpanded(false)
        }
    }

    private fun addToSearchList(element: SearchItem) {
        var isDuplicated = false
        val tempModel = element.model
        val tempCsc = element.csc

        if (searchList.isNotEmpty()) {
            for (i in searchList.indices) {
                if (tempModel == searchList[i].model && tempCsc == searchList[i].csc) {
                    isDuplicated = true
                }
            }
        }

        if (!isDuplicated) {
            searchList.add(element)
        }
    }
}