package com.illusion.checkfirm.bookmark

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.illusion.checkfirm.dialogs.BookmarkDialog
import com.illusion.checkfirm.R
import com.illusion.checkfirm.database.BookmarkDB
import com.illusion.checkfirm.database.BookmarkDBHelper
import java.util.ArrayList

class BookmarkActivity : AppCompatActivity() {

    private lateinit var mAdapter: BookmarkAdapter
    private val mBookmarkList = ArrayList<BookmarkDB>()
    private lateinit var mDB: BookmarkDBHelper
    private lateinit var mRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)

        initToolbar()

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val bottomSheetFragment = BookmarkDialog.newInstance(false, "", "", "", -1)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        mRecyclerView = findViewById(R.id.mRecyclerView)
        mAdapter = BookmarkAdapter(mBookmarkList, object : BookmarkAdapter.MyAdapterListener {
            override fun onLayoutClicked(v: View, position: Int) {
                val model = mBookmarkList[position].model!!
                val csc = mBookmarkList[position].csc!!
                if (model.isBlank() || csc.isBlank()) {
                    Toast.makeText(this@BookmarkActivity, R.string.bookmark_search_error, Toast.LENGTH_SHORT).show()
                } else {
                    intent.putExtra("model", model)
                    intent.putExtra("csc", csc)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }

            override fun onEditClicked(v: View, position: Int) {
                val bottomSheetFragment = BookmarkDialog.newInstance(true, mBookmarkList[position].name.toString(), mBookmarkList[position].model.toString(), mBookmarkList[position].csc.toString(), position)
                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            }

            override fun onDeleteClicked(v: View, position: Int) {
                mDB.deleteBookMark(mBookmarkList[position])
                mBookmarkList.removeAt(position)
                mBookmarkList.clear()
                mBookmarkList.addAll(mDB.allBookmarkDB)
                mAdapter.notifyDataSetChanged()
            }
        })
        val mLayoutManager = LinearLayoutManager(applicationContext)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mAdapter

        mDB = BookmarkDBHelper(this)
        mBookmarkList.addAll(mDB.allBookmarkDB)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            mBookmarkList.clear()
            mBookmarkList.addAll(mDB.allBookmarkDB)
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun initToolbar() {
        val one = getSharedPreferences("settings", Context.MODE_PRIVATE).getBoolean("one", true)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        val mAppBar = findViewById<AppBarLayout>(R.id.appbar)
        val height = (resources.displayMetrics.heightPixels * 0.3976)
        val lp = mAppBar.layoutParams
        lp.height = height.toInt()
        if (one) {
            mAppBar.setExpanded(true)
        } else {
            mAppBar.setExpanded(false)
        }

        val title = findViewById<TextView>(R.id.title)
        val expandedTitle = findViewById<TextView>(R.id.expanded_title)
        mAppBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, _ ->
            val percentage = (appBarLayout.y / appBarLayout.totalScrollRange)
            expandedTitle.alpha = 1 - (percentage * 2 * -1)
            title.alpha = percentage * -1
        })
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
}