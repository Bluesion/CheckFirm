package com.illusion.checkfirm.bookmark

import android.app.Activity
import android.content.Context
import android.os.Bundle
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
import java.util.*

class BookmarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookmarkBinding
    lateinit var viewModel: BookmarkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()

        binding.fab.setOnClickListener {
            val bottomSheetFragment = BookmarkDialog.newInstance(false, 0, "", "", "")
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        val mAdapter = BookmarkAdapter(ArrayList(), object : BookmarkAdapter.MyAdapterListener {
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
        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerView.adapter = mAdapter

        viewModel = ViewModelProvider(this, CheckFirm.viewModelFactory).get(BookmarkViewModel::class.java)
        viewModel.allBookmarks.observe(this, androidx.lifecycle.Observer { devices ->
            devices?.let { mAdapter.setBookmarks(it) }
        })
        viewModel.getCount()!!.observe(this, androidx.lifecycle.Observer { counts ->
            counts?.let {
                binding.expandedSubtitle.text = resources.getQuantityString(R.plurals.bookmark_subtitle, it, it)
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

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)

        val toolbarText = getString(R.string.bookmark)
        binding.title.text = toolbarText
        binding.expandedTitle.text = toolbarText

        binding.appbar.layoutParams.height = (resources.displayMetrics.heightPixels * 0.3976).toInt()
        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, _ ->
            val percentage = (appBarLayout.y / appBarLayout.totalScrollRange)
            binding.expandedTitle.alpha = 1 - (percentage * 2 * -1)
            binding.expandedSubtitle.alpha = 1 - (percentage * 2 * -1)
            binding.title.alpha = percentage * -1
        })

        val one = getSharedPreferences("settings", Context.MODE_PRIVATE).getBoolean("one", true)
        if (one) {
            binding.appbar.setExpanded(true)
        } else {
            binding.appbar.setExpanded(false)
        }
    }
}