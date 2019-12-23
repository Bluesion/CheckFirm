package com.illusion.checkfirm.bookmark

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.illusion.checkfirm.R
import com.illusion.checkfirm.database.bookmark.BookmarkViewModel
import com.illusion.checkfirm.dialogs.BookmarkDialog
import java.util.*

class BookmarkActivity : AppCompatActivity() {

    private lateinit var mAdapter: BookmarkAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var expandedSubTitle: TextView
    lateinit var viewModel: BookmarkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)

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
            expandedSubTitle.alpha = 1 - (percentage * 2 * -1)
            title.alpha = percentage * -1
        })
        expandedSubTitle = findViewById(R.id.expanded_subtitle)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val bottomSheetFragment = BookmarkDialog.newInstance(false, 0, "", "", "")
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        mRecyclerView = findViewById(R.id.mRecyclerView)
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
        val mLayoutManager = LinearLayoutManager(applicationContext)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mAdapter

        viewModel = ViewModelProviders.of(this).get(BookmarkViewModel::class.java)
        viewModel.allBookmarks.observe(this, androidx.lifecycle.Observer { devices ->
            devices?.let { mAdapter.setBookmarks(it) }
        })
        viewModel.getCount()!!.observe(this, androidx.lifecycle.Observer { counts ->
            counts?.let {
                expandedSubTitle.text = resources.getQuantityString(R.plurals.bookmark_subtitle, it, it)
            }
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