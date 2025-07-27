package com.illusion.checkfirm.features.settings.backuprestore

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmActivity
import com.illusion.checkfirm.data.model.BackupRestoreItem
import com.illusion.checkfirm.databinding.ActivityBookmarkBackupRestoreBinding
import com.illusion.checkfirm.features.bookmark.viewmodel.BookmarkViewModel
import com.illusion.checkfirm.features.bookmark.viewmodel.CategoryViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BackupRestoreActivity : CheckFirmActivity<ActivityBookmarkBackupRestoreBinding>() {

    private val bookmarkViewModel: BookmarkViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val startBackup =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.also { uri ->
                    backup(uri.data!!)
                }
            }
        }

    private val startRestore =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.also { uri ->
                    restore(uri.data!!)
                }
            }
        }

    private lateinit var json: Json

    override fun createBinding() = ActivityBookmarkBackupRestoreBinding.inflate(layoutInflater)

    override fun setContentInset() {
        setBottomInset(binding.imageDeviceLine)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(binding.toolbar, "")

        json = Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
        }

        binding.backupButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val date = SimpleDateFormat("yyyyMMdd", Locale.KOREAN).format(calendar.time)

            startBackup.launch(Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/json"
                putExtra(Intent.EXTRA_TITLE, "CheckFirm_backup_${date}.json")
            })
        }

        binding.restoreButton.setOnClickListener {
            startRestore.launch(Intent(Intent.ACTION_GET_CONTENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/json"
            })
        }
    }

    private fun backup(uri: Uri) {
        lifecycleScope.launch {
            val backupItem = BackupRestoreItem()

            backupItem.bookmarkList = bookmarkViewModel.getAllBookmarkList()
            backupItem.categoryList = categoryViewModel.getAllCategoryList()

            val backupJson = json.encodeToString(backupItem)
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                BufferedWriter(OutputStreamWriter(outputStream)).use { reader ->
                    reader.write(backupJson)
                }
            }

            Toast.makeText(
                this@BackupRestoreActivity, getString(R.string.backup_toast), Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }

    private fun restore(uri: Uri) {
        val stringBuilder = StringBuilder()

        contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line: String? = reader.readLine()
                while (line != null) {
                    stringBuilder.append(line)
                    line = reader.readLine()
                }
            }
        }

        val restoreItem = json.decodeFromString<BackupRestoreItem>(stringBuilder.toString())

        for (element in restoreItem.bookmarkList) {
            bookmarkViewModel.addBookmark(element.name, element.model, element.csc, element.category)
        }

        for (element in restoreItem.categoryList) {
            categoryViewModel.addCategory(element.name)
        }

        Toast.makeText(this, getString(R.string.restore_toast), Toast.LENGTH_SHORT).show()
        finish()
    }
}
