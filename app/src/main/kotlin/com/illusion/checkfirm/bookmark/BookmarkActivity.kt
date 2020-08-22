package com.illusion.checkfirm.bookmark

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.database.bookmark.BookmarkViewModel
import com.illusion.checkfirm.databinding.ActivityBookmarkBinding
import com.illusion.checkfirm.dialogs.BookmarkDialog
import com.illusion.checkfirm.dialogs.OrderDialog
import java.util.*

class BookmarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookmarkBinding
    lateinit var viewModel: BookmarkViewModel
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var mAdapter: BookmarkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()

        binding.fab.setOnClickListener {
            val bottomSheetFragment = BookmarkDialog.newInstance(false, 0, "", "", "")
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        viewModel = ViewModelProvider(this, CheckFirm.viewModelFactory).get(BookmarkViewModel::class.java)
        viewModel.getCount()!!.observe(this, androidx.lifecycle.Observer { counts ->
            counts?.let {
                binding.expandedSubtitle.text = resources.getQuantityString(R.plurals.bookmark_subtitle, it, it)
            }
        })

        mAdapter = BookmarkAdapter(ArrayList(), object : BookmarkAdapter.MyAdapterListener {
            override fun onLayoutClicked(model: String, csc: String) {
                if (model.isBlank() || csc.isBlank()) {
                    Toast.makeText(this@BookmarkActivity, R.string.bookmark_search_error, Toast.LENGTH_SHORT).show()
                } else {
                    intent.putExtra("model", model)
                    intent.putExtra("csc", csc)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }

            override fun onEditClicked(id: Long, name: String, model: String, csc: String) {
                val bottomSheetFragment = BookmarkDialog.newInstance(true, id, name, model, csc)
                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            }

            override fun onDeleteClicked(device: String) {
                viewModel.delete(device)
            }
        })
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = mAdapter

        initList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bookmark, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.order -> {
                val bottomSheetFragment = OrderDialog()
                bottomSheetFragment.setOnBottomSheetCloseListener(object : OrderDialog.OnBottomSheetCloseListener {
                    override fun onBottomSheetClose() {
                        initList()
                    }
                })
                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)

        val title = binding.title
        val expandedTitle = binding.expandedTitle

        val toolbarText = getString(R.string.bookmark)
        title.text = toolbarText
        expandedTitle.text = toolbarText

        val appbar = binding.appBar
        appbar.layoutParams.height = (resources.displayMetrics.heightPixels * 0.3976).toInt()
        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, _ ->
            val percentage = (appBarLayout.y / appBarLayout.totalScrollRange)
            expandedTitle.alpha = 1 - (percentage * 2 * -1)
            binding.expandedSubtitle.alpha = 1 - (percentage * 2 * -1)
            title.alpha = percentage * -1
        })

        sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val one = sharedPrefs.getBoolean("one", true)
        if (one) {
            appbar.setExpanded(true)
        } else {
            appbar.setExpanded(false)
        }
    }

    private fun initList() {
        val bookmarkOrderBy = sharedPrefs.getString("bookmark_order_by", "time")!!
        val isDescending = sharedPrefs.getBoolean("bookmark_order_by_desc", false)

        viewModel.getBookmarks(bookmarkOrderBy, isDescending).observe(this, androidx.lifecycle.Observer { bookmarks ->
            bookmarks?.let { mAdapter.setBookmarks(it) }
        })
    }
}
