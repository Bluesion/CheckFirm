package com.illusion.checkfirm.features.bookmark.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmActivity
import com.illusion.checkfirm.data.model.CategoryDeviceListItem
import com.illusion.checkfirm.databinding.ActivityCategoryEditBinding
import com.illusion.checkfirm.features.bookmark.viewmodel.BookmarkViewModel
import com.illusion.checkfirm.features.bookmark.viewmodel.CategoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryEditActivity : CheckFirmActivity<ActivityCategoryEditBinding>() {

    private val bookmarkViewModel: BookmarkViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private var devicesList = ArrayList<CategoryDeviceListItem>()

    override fun createBinding() = ActivityCategoryEditBinding.inflate(layoutInflater)

    override fun setContentInset() {
        setBottomInset(binding.save)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(binding.toolbar, "")

        var category = intent.getStringExtra("name")
        if (category.isNullOrBlank()) {
            category = ""
        } else {
            binding.categoryName.setText(category)
            binding.categoryName.setSelection(binding.categoryName.text!!.length)
        }

        val deviceAdapter = CategoryDeviceAdapter()
        binding.categoryDevicesRecyclerView.adapter = deviceAdapter

        lifecycleScope.launch(Dispatchers.Default) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                bookmarkViewModel.bookmarkList.collect {
                    devicesList.clear()
                    for (element in it) {
                        if (element.category.isBlank() || element.category == category) {
                            devicesList.add(
                                CategoryDeviceListItem(
                                    element,
                                    element.category == category
                                )
                            )
                        }
                    }
                    withContext(Dispatchers.Main) {
                        deviceAdapter.setList(devicesList)
                    }
                }
            }
        }

        binding.save.setOnClickListener {
            if (binding.categoryName.text.toString().isBlank()) {
                Toast.makeText(
                    this,
                    getString(R.string.category_edit_name_error_empty),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val tempList = ArrayList<CategoryDeviceListItem>()
                for (element in devicesList) {
                    if (element.isChecked) {
                        tempList.add(element)
                    }
                }

                if (tempList.isEmpty()) {
                    Toast.makeText(
                        this,
                        getString(R.string.category_edit_device_list_error_empty),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    for (element in devicesList) {
                        if (element.isChecked) {
                            bookmarkViewModel.editBookmark(
                                element.bookmark.id!!,
                                element.bookmark.name,
                                element.bookmark.model,
                                element.bookmark.csc,
                                binding.categoryName.text.toString(),
                                element.bookmark.position
                            )
                        } else {
                            if (element.bookmark.category == category) {
                                bookmarkViewModel.editBookmark(
                                    element.bookmark.id!!, element.bookmark.name,
                                    element.bookmark.model, element.bookmark.csc, "", element.bookmark.position
                                )
                            }
                        }
                    }

                    if (intent.getBooleanExtra("isAdd", false)) {
                        categoryViewModel.addCategory(binding.categoryName.text.toString())
                    } else {
                        categoryViewModel.editCategory(
                            intent.getLongExtra("id", 0), binding.categoryName.text.toString(),
                            intent.getIntExtra("position", 0)
                        )
                    }

                    finish()
                }
            }
        }
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
