package com.illusion.checkfirm.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.InputFilter
import android.view.View
import java.util.*
import android.widget.EditText
import com.illusion.checkfirm.utils.ExceptionHandler
import com.illusion.checkfirm.R
import com.illusion.checkfirm.adapters.BookMarkAdapter
import com.illusion.checkfirm.database.BookMark
import com.illusion.checkfirm.database.DatabaseHelper
import com.illusion.checkfirm.utils.RecyclerTouchListener
import android.content.Intent
import android.content.res.Resources

class BookMarkActivity : AppCompatActivity() {

    private lateinit var mAdapter: BookMarkAdapter
    private val mBookMarkList = ArrayList<BookMark>()
    private lateinit var mDB: DatabaseHelper
    lateinit var mRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))
        setContentView(R.layout.activity_bookmark)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { showBookMarkDialog(false, null, -1) }

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
                showActionsDialog(position)
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

    private fun createBookMark(name: String, model: String, csc: String) {
        val id = mDB.insertBookMark(name, model, csc)

        val n = mDB.getBookMark(id)
        mBookMarkList.add(n)
        mAdapter.notifyDataSetChanged()
    }

    private fun updateBookMark(name: String, model: String, csc: String, position: Int) {
        val b = mBookMarkList[position]
        b.name = name
        b.model = model
        b.csc = csc

        mDB.updateBookMark(b)

        mBookMarkList[position] = b
        mAdapter.notifyItemChanged(position)
    }

    private fun deleteBookMark(position: Int) {
        mDB.deleteBookMark(mBookMarkList[position])

        mBookMarkList.removeAt(position)
        mAdapter.notifyItemRemoved(position)
    }

    private fun showActionsDialog(position: Int) {
        val colors = arrayOf<CharSequence>("수정", "삭제")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("옵션")
        builder.setItems(colors) { _, which ->
            if (which == 0) {
                showBookMarkDialog(true, mBookMarkList[position], position)
            } else {
                deleteBookMark(position)
            }
        }
        builder.show()
    }

    @SuppressLint("RestrictedApi")
    private fun showBookMarkDialog(shouldUpdate: Boolean, bookmark: BookMark?, position: Int) {
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.alertdialog, null, false)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("디바이스 추가")
        val margin = dpToPx(20)
        builder.setView(dialogView, margin, margin, margin, 0)
        val inputName = dialogView.findViewById<EditText>(R.id.name)
        val inputModel = dialogView.findViewById<EditText>(R.id.model)
        val inputCSC = dialogView.findViewById<EditText>(R.id.csc)

        val modelFilters = inputModel.filters
        val newModelFilters = arrayOfNulls<InputFilter>(modelFilters.size + 1)
        System.arraycopy(modelFilters, 0, newModelFilters, 0, modelFilters.size)
        newModelFilters[modelFilters.size] = InputFilter.AllCaps()
        inputModel.filters = newModelFilters

        val cscFilters = inputCSC.filters
        val newCscFilters2 = arrayOfNulls<InputFilter>(cscFilters.size + 1)
        System.arraycopy(cscFilters, 0, newCscFilters2, 0, cscFilters.size)
        newCscFilters2[cscFilters.size] = InputFilter.AllCaps()
        inputCSC.filters = newCscFilters2

        if (shouldUpdate && bookmark != null) {
            inputName.setText(bookmark.name)
            inputModel.setText(bookmark.model)
            inputCSC.setText(bookmark.csc)
        }
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            val name = inputName.text.toString()
            val model = inputModel.text.toString()
            val csc = inputCSC.text.toString()

            if (shouldUpdate && bookmark != null) {
                updateBookMark(name, model, csc, position)
            } else {
                createBookMark(name, model, csc)
            }
        }
        builder.setNegativeButton(android.R.string.cancel) { _, _ -> }
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
}