package com.illusion.checkfirm.features.bookmark.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmActivity
import com.illusion.checkfirm.common.util.Tools
import com.illusion.checkfirm.databinding.ActivityBookmarkCategoryBinding
import com.illusion.checkfirm.features.bookmark.viewmodel.BookmarkViewModel
import com.illusion.checkfirm.features.bookmark.viewmodel.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarkCategoryActivity : CheckFirmActivity<ActivityBookmarkCategoryBinding>() {

    private val bookmarkViewModel by viewModels<BookmarkViewModel>()

    private val categoryViewModel by viewModels<CategoryViewModel>()

    private var currentTab = MutableStateFlow(-1)

    override fun createBinding() = ActivityBookmarkCategoryBinding.inflate(layoutInflater)

    override fun setContentInset() {
        setVerticalInset(binding.mainView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(binding.includeToolbar.appBar, getString(R.string.bookmark), true)

        binding.tabBookmark.setOnClickListener {
            currentTab.value = 0
        }

        binding.tabCategory.setOnClickListener {
            currentTab.value = 1
        }

        lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                currentTab.collect {
                    if (it == 0) {
                        initBookmarkLayout()
                    } else {
                        initCategoryLayout()
                    }
                }
            }
        }
        currentTab.value = 0
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {
                if (currentTab.value == 0) {
                    BookmarkDialog(onDialogClose = {
                        bookmarkViewModel.addOrEditBookmark(it)
                    }).show(supportFragmentManager, null)
                } else {
                    val intent = Intent(this, CategoryEditActivity::class.java)
                    intent.putExtra("isAdd", true)
                    startActivity(intent)
                }
                return true
            }

            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private suspend fun initBookmarkLayout() {
        val bookmarkCount = bookmarkViewModel.getBookmarkCount()

        binding.tabBookmark.setTabSelected(true)
        binding.tabCategory.setTabSelected(false)

        // 툴바 상태 바꾸기
        binding.includeToolbar.title.text = getString(R.string.bookmark)
        binding.includeToolbar.expandedTitle.text = getString(R.string.bookmark)
        binding.includeToolbar.expandedSubTitle.text = resources.getQuantityString(
            R.plurals.bookmark_subtitle, bookmarkCount, bookmarkCount
        )

        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainerView.id, BookmarkFragment()).commit()
    }

    private suspend fun initCategoryLayout() {
        val categoryCount = categoryViewModel.getCategoryCount()

        // 탭 상태 바꾸기
        binding.tabBookmark.setTabSelected(false)
        binding.tabCategory.setTabSelected(true)

        // 툴바 상태 바꾸기
        binding.includeToolbar.title.text = getString(R.string.category)
        binding.includeToolbar.expandedTitle.text = getString(R.string.category)
        binding.includeToolbar.expandedSubTitle.text = resources.getQuantityString(
            R.plurals.category_subtitle, categoryCount, categoryCount
        )

        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainerView.id, CategoryFragment()).commit()
    }

    fun onBookmarkCardClicked(model: String, csc: String) {
        if (Tools.isValidDevice(model, csc)) {
            intent.putExtra("model", model)
            intent.putExtra("csc", csc)
            setResult(RESULT_OK, intent)
            finish()
        } else {
            Toast.makeText(
                this,
                R.string.bookmark_search_error,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}