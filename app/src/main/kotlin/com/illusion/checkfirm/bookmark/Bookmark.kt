package com.illusion.checkfirm.bookmark

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.illusion.checkfirm.R
import com.illusion.checkfirm.database.BookmarkDB
import com.illusion.checkfirm.database.BookmarkDBHelper
import com.illusion.checkfirm.dialogs.BookmarkDialog
import com.illusion.checkfirm.search.Search
import com.illusion.checkfirm.search.TransparentActivity
import java.util.ArrayList

class Bookmark : Fragment() {

    private lateinit var mAdapter: BookmarkAdapter
    private val mBookMarkList = ArrayList<BookmarkDB>()
    private lateinit var mDB: BookmarkDBHelper
    private lateinit var mRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_bookmark, container, false)

        val fab = rootView.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val bottomSheetFragment = BookmarkDialog.newInstance(false, "", "", "", -1)
            bottomSheetFragment.show(activity!!.supportFragmentManager, bottomSheetFragment.tag)
        }

        mRecyclerView = rootView.findViewById(R.id.mRecyclerView)
        mAdapter = BookmarkAdapter(mBookMarkList, object : BookmarkAdapter.MyAdapterListener {
            override fun onEditClicked(v: View, position: Int) {
                val bottomSheetFragment = BookmarkDialog.newInstance(true, mBookMarkList[position].name.toString(), mBookMarkList[position].model.toString(), mBookMarkList[position].csc.toString(), position)
                bottomSheetFragment.show(activity!!.supportFragmentManager, bottomSheetFragment.tag)
            }

            override fun onDeleteClicked(v: View, position: Int) {
                mDB.deleteBookMark(mBookMarkList[position])
                mBookMarkList.removeAt(position)
                mBookMarkList.clear()
                mBookMarkList.addAll(mDB.allBookmarkDB)
                mAdapter.notifyDataSetChanged()
            }
        })
        val mLayoutManager = LinearLayoutManager(activity!!.applicationContext)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mRecyclerView.adapter = mAdapter

        mDB = BookmarkDBHelper(activity!!)
        mBookMarkList.addAll(mDB.allBookmarkDB)

        rootView.viewTreeObserver.addOnWindowFocusChangeListener{
            mBookMarkList.clear()
            mBookMarkList.addAll(mDB.allBookmarkDB)
            mAdapter.notifyDataSetChanged()
        }

        return rootView
    }
}