package com.tanzentlab.checksamfirm.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tanzentlab.checksamfirm.R
import com.tanzentlab.checksamfirm.adapters.OptionsAdapter
import com.tanzentlab.checksamfirm.database.BookMark
import com.tanzentlab.checksamfirm.database.DatabaseHelper
import com.tanzentlab.checksamfirm.utils.RecyclerTouchListener

class OptionsFragment: BottomSheetDialogFragment() {

    private lateinit var mRecyclerView: RecyclerView
    private val optionsList = ArrayList<Options>()
    private lateinit var mAdapter: OptionsAdapter
    private val mBookMarkList = ArrayList<BookMark>()
    private lateinit var mDB: DatabaseHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.AppTheme)
        val rootView = inflater.cloneInContext(contextThemeWrapper).inflate(R.layout.bottomsheet_options, container, false)

        val option1 = Options(R.drawable.ic_edit, getString(R.string.edit))
        optionsList.add(option1)
        val option2 = Options(R.drawable.ic_delete, getString(R.string.delete))
        optionsList.add(option2)

        mDB = DatabaseHelper(activity!!.applicationContext)
        mBookMarkList.addAll(mDB.allBookMark)

        val myDay= arguments!!.getInt("position")

        mRecyclerView = rootView.findViewById(R.id.mRecyclerView)
        mAdapter = OptionsAdapter(optionsList)
        val mLayoutManager = LinearLayoutManager(activity!!.applicationContext)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mRecyclerView.adapter = mAdapter
        mRecyclerView.addOnItemTouchListener(RecyclerTouchListener(activity!!.applicationContext, mRecyclerView, object: RecyclerTouchListener.ClickListener {
            override fun onClick(view: View, position: Int) {
                if (position == 0) {
                    val bottomSheetFragment = AddEditFragment.newInstance(true, mBookMarkList[myDay].name.toString(), mBookMarkList[myDay].model.toString(), mBookMarkList[myDay].csc.toString(), myDay)
                    bottomSheetFragment.show(activity!!.supportFragmentManager, bottomSheetFragment.tag)
                    dismiss()
                } else if (position == 1) {
                    deleteBookMark(myDay)
                    dismiss()
                }
            }

            override fun onLongClick(view: View?, position: Int) {}
        }))

        return rootView
    }

    private fun deleteBookMark(position: Int) {
        mDB.deleteBookMark(mBookMarkList[position])
        mBookMarkList.removeAt(position)
    }

    companion object {
        fun newInstance(position: Int): OptionsFragment {
            val f = OptionsFragment()

            val args = Bundle()
            args.putInt("position", position)
            f.arguments = args

            return f
        }
    }
}