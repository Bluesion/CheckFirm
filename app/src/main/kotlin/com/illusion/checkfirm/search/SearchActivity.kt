package com.illusion.checkfirm.search

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.database.bookmark.BookmarkViewModel
import com.illusion.checkfirm.databinding.ActivitySearchBinding
import com.illusion.checkfirm.utils.Tools
import java.text.SimpleDateFormat
import java.util.*

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var settingPrefs: SharedPreferences
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        historyPrefs = getSharedPreferences("search_history", Context.MODE_PRIVATE)
        historyEditor = historyPrefs.edit()
        settingPrefs.edit().putString("search_mode", "single").apply()
        imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        initToolbar()

        // MULTI SEARCH
        val multiLayout = binding.multiLayout
        val searchLists = binding.searchLists
        searchLists.text = String.format(getString(R.string.multi_search_lists), 0)
        searchRecyclerView = binding.searchRecyclerView
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
        searchRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        searchRecyclerView.adapter = searchAdapter

        val addButton = binding.add
        addButton.setOnClickListener {
            if (searchList.size >= 4) {
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

        binding.searchButton.setOnClickListener {
            search()
        }

        // COMMON
        model = binding.model
        csc = binding.csc
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
                        if (settingPrefs.getString("search_mode", "single") == "single") {
                            searchList.add(item)
                            search()
                        } else {
                            if (searchList.size >= 4) {
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

        val bookmarkChipGroup = binding.chipGroup

        val bookmarkOrderBy = settingPrefs.getString("bookmark_order_by", "time")!!
        val isDescending = settingPrefs.getBoolean("bookmark_order_by_desc", false)

        val viewModel = ViewModelProvider(this, CheckFirm.viewModelFactory).get(BookmarkViewModel::class.java)
        viewModel.getBookmarks(bookmarkOrderBy, isDescending).observe(this, {
            if (it.isEmpty()) {
                if (historyList.isEmpty()) {
                    binding.quick.visibility = View.GONE
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
                            if (settingPrefs.getString("search_mode", "single") == "single") {
                                Toast.makeText(this, R.string.info_catcher_error, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val item = SearchItem(modelString, cscString)
                            if (settingPrefs.getString("search_mode", "single") == "single") {
                                searchList.add(item)
                                search()
                            } else {
                                if (searchList.size >= 4) {
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

        historyRecyclerView = binding.historyRecyclerView
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
                    if (settingPrefs.getString("search_mode", "single") == "single") {
                        searchList.add(item)
                        search()
                    } else {
                        if (searchList.size >= 4) {
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

        historyRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
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
        val searchButton = binding.search
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

        val tab1 = binding.singleSearch
        val tab2 = binding.multiSearch
        tab1.setTextAppearance(R.style.SearchButton_Selected)
        tab2.setTextAppearance(R.style.SearchButton_Unselected)

        tab1.setOnClickListener {
            tab1.setTextAppearance(R.style.SearchButton_Selected)
            tab2.setTextAppearance(R.style.SearchButton_Unselected)
            settingPrefs.edit().putString("search_mode", "single").apply()

            searchButton.visibility = View.VISIBLE
            addButton.visibility = View.GONE
            multiLayout.visibility = View.GONE
        }

        tab2.setOnClickListener {
            tab1.setTextAppearance(R.style.SearchButton_Unselected)
            tab2.setTextAppearance(R.style.SearchButton_Selected)
            settingPrefs.edit().putString("search_mode", "multi").apply()

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
        setSupportActionBar(binding.includeToolbar.toolbar)

        val toolbarText = getString(R.string.search)
        val title = binding.includeToolbar.title
        title.text = toolbarText
        val expandedTitle = binding.includeToolbar.expandedTitle
        expandedTitle.text = toolbarText

        val appBar = binding.includeToolbar.appBar
        appBar.layoutParams.height = (resources.displayMetrics.heightPixels * 0.3976).toInt()
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, _ ->
            val percentage = (appBarLayout.y / appBarLayout.totalScrollRange)
            expandedTitle.alpha = 1 - (percentage * 2 * -1)
            title.alpha = percentage * -1
        })
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
