package com.illusion.checkfirm.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.illusion.checkfirm.R
import com.illusion.checkfirm.adapters.BookMarkAdapter
import com.illusion.checkfirm.database.BookMark
import com.illusion.checkfirm.database.DatabaseHelper
import com.illusion.checkfirm.dialogs.AddEditFragment
import com.illusion.checkfirm.dialogs.OptionsFragment
import com.illusion.checkfirm.utils.ExceptionHandler
import com.illusion.checkfirm.utils.RecyclerTouchListener
import com.illusion.checkfirm.utils.ThemeChanger
import java.util.*

class BookMarkActivity : AppCompatActivity() {

    private lateinit var mAdapter: BookMarkAdapter
    private val mBookMarkList = ArrayList<BookMark>()
    private lateinit var mDB: DatabaseHelper
    private lateinit var mRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))
        ThemeChanger.setAppTheme(this)
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