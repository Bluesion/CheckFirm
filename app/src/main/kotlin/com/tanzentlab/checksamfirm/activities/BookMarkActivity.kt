package com.tanzentlab.checksamfirm.activities

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import java.util.*
import com.tanzentlab.checksamfirm.utils.ExceptionHandler
import com.tanzentlab.checksamfirm.R
import com.tanzentlab.checksamfirm.adapters.BookMarkAdapter
import com.tanzentlab.checksamfirm.database.BookMark
import com.tanzentlab.checksamfirm.database.DatabaseHelper
import com.tanzentlab.checksamfirm.utils.RecyclerTouchListener
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tanzentlab.checksamfirm.dialogs.AddEditFragment
import com.tanzentlab.checksamfirm.dialogs.OptionsFragment

class BookMarkActivity : AppCompatActivity() {

    private lateinit var mAdapter: BookMarkAdapter
    private val mBookMarkList = ArrayList<BookMark>()
    private lateinit var mDB: DatabaseHelper
    private lateinit var mRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))
        setContentView(R.layout.activity_bookmark)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val bottomSheetFragment = AddEditFragment.newInstance(false, "", "", "", -1)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        mRecyclerView = findViewById(R.id.mRecyclerView)
        mAdapter = BookMarkAdapter(mBookMarkList)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mRecyclerView.adapter = mAdapter
        mRecyclerView.addOnItemTouchListener(RecyclerTouchListener(this, mRecyclerView, object: RecyclerTouchListener.ClickListener {
            override fun onClick(view: View, position: Int) {
                if (position != RecyclerView.NO_POSITION) {
                    val intent = Intent(this@BookMarkActivity, MainActivity::class.java)
                    intent.putExtra("model", mBookMarkList[position].model)
                    intent.putExtra("csc", mBookMarkList[position].csc)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    this@BookMarkActivity.startActivity(intent)
                }
            }

            override fun onLongClick(view: View?, position: Int) {
                val bottomSheetFragment = OptionsFragment.newInstance(position)
                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            }
        }))

        mDB = DatabaseHelper(this)
        mBookMarkList.addAll(mDB.allBookMark)
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

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (hasFocus) {
            mBookMarkList.clear()
            mBookMarkList.addAll(mDB.allBookMark)
            mAdapter.notifyDataSetChanged()
        }
    }
}