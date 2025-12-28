package com.illusion.checkfirm.features.sherlock.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmActivity
import com.illusion.checkfirm.data.model.local.SearchResultItem
import com.illusion.checkfirm.databinding.ActivitySherlockBinding
import com.illusion.checkfirm.features.sherlock.util.SherlockStatus
import com.illusion.checkfirm.features.sherlock.viewmodel.SherlockViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SherlockActivity : CheckFirmActivity<ActivitySherlockBinding>() {

    private val sherlockViewModel by viewModels<SherlockViewModel>()

    override fun createBinding() = ActivitySherlockBinding.inflate(layoutInflater)

    override fun setContentInset() {
        setBottomInset(binding.tabManual)
        setBottomInset(binding.tabScript)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(binding.toolbar, getString(R.string.sherlock))

        val searchResult = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("search_result", SearchResultItem::class.java)
        } else {
            intent.getParcelableExtra("search_result") as? SearchResultItem
        }

        if (searchResult == null) {
            Toast.makeText(this, "ERROR: SEARCH RESULT NULL", Toast.LENGTH_SHORT).show()
            return
        }

        sherlockViewModel.initialize(searchResult)

        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        binding.successCardCopy.setOnClickListener {
            val clip = ClipData.newPlainText("CheckFirm", binding.successCardText.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, R.string.clipboard, Toast.LENGTH_SHORT).show()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sherlockViewModel.sherlockStatus.collect { status ->
                    when (status) {
                        SherlockStatus.INITIAL -> {
                            val animatedVector = ContextCompat.getDrawable(
                                this@SherlockActivity,
                                R.drawable.ic_sherlock_normal_face
                            ) as AnimatedVectorDrawable
                            binding.statusImage.setImageDrawable(animatedVector)
                            animatedVector.start()
                            binding.successCard.visibility = View.GONE
                        }

                        SherlockStatus.FAIL -> {
                            binding.tabManual.isEnabled = true
                            binding.tabScript.isEnabled = true
                            val animatedVector = ContextCompat.getDrawable(
                                this@SherlockActivity,
                                R.drawable.ic_sherlock_fail_face
                            ) as AnimatedVectorDrawable
                            binding.statusImage.setImageDrawable(animatedVector)
                            animatedVector.start()
                            binding.statusText.text = getString(R.string.sherlock_result_fail)
                            binding.successCard.visibility = View.GONE
                        }

                        SherlockStatus.RUNNING -> {
                            binding.tabManual.isEnabled = false
                            binding.tabScript.isEnabled = false
                            val animatedVector = ContextCompat.getDrawable(
                                this@SherlockActivity,
                                R.drawable.ic_sherlock_loading_face
                            ) as AnimatedVectorDrawable
                            binding.statusImage.setImageDrawable(animatedVector)
                            animatedVector.start()
                            binding.statusText.text = getString(R.string.sherlock_script_running)
                            binding.successCard.visibility = View.GONE
                        }

                        SherlockStatus.SUCCESS -> {
                            binding.tabManual.isEnabled = true
                            binding.tabScript.isEnabled = true
                            val animatedVector = ContextCompat.getDrawable(
                                this@SherlockActivity,
                                R.drawable.ic_sherlock_success_face
                            ) as AnimatedVectorDrawable
                            binding.statusImage.setImageDrawable(animatedVector)
                            animatedVector.start()
                            binding.statusText.text = getString(R.string.sherlock_result_success)
                            binding.successCardText.text = sherlockViewModel.userInput.value
                            binding.successCard.visibility = View.VISIBLE
                        }

                        SherlockStatus.NO_WARNING -> {
                            val animatedVector = ContextCompat.getDrawable(
                                this@SherlockActivity,
                                R.drawable.ic_sherlock_normal_face
                            ) as AnimatedVectorDrawable
                            binding.statusImage.setImageDrawable(animatedVector)
                            animatedVector.start()
                            binding.statusText.text = getString(R.string.sherlock_script_no_error)
                        }

                        SherlockStatus.WARNING_SCRIPT_START_INVALID -> {
                            val animatedVector = ContextCompat.getDrawable(
                                this@SherlockActivity,
                                R.drawable.ic_sherlock_warning_face
                            ) as AnimatedVectorDrawable
                            binding.statusImage.setImageDrawable(animatedVector)
                            animatedVector.start()
                            binding.statusText.text = getString(R.string.sherlock_script_error_7)
                        }

                        SherlockStatus.WARNING_SCRIPT_END_INVALID -> {
                            val animatedVector = ContextCompat.getDrawable(
                                this@SherlockActivity,
                                R.drawable.ic_sherlock_warning_face
                            ) as AnimatedVectorDrawable
                            binding.statusImage.setImageDrawable(animatedVector)
                            animatedVector.start()
                            binding.statusText.text = getString(R.string.sherlock_script_error_8)
                        }

                        SherlockStatus.WARNING_BUILD_NUMBER_BOOTLOADER -> {
                            val animatedVector = ContextCompat.getDrawable(
                                this@SherlockActivity,
                                R.drawable.ic_sherlock_warning_face
                            ) as AnimatedVectorDrawable
                            binding.statusImage.setImageDrawable(animatedVector)
                            animatedVector.start()
                            binding.statusText.text = getString(R.string.sherlock_script_error_1)
                        }

                        SherlockStatus.WARNING_BUILD_NUMBER_ONE_UI_VERSION -> {
                            val animatedVector = ContextCompat.getDrawable(
                                this@SherlockActivity,
                                R.drawable.ic_sherlock_warning_face
                            ) as AnimatedVectorDrawable
                            binding.statusImage.setImageDrawable(animatedVector)
                            animatedVector.start()
                            binding.statusText.text = getString(R.string.sherlock_script_error_3)
                        }

                        SherlockStatus.WARNING_BUILD_NUMBER_YEAR -> {
                            val animatedVector = ContextCompat.getDrawable(
                                this@SherlockActivity,
                                R.drawable.ic_sherlock_warning_face
                            ) as AnimatedVectorDrawable
                            binding.statusImage.setImageDrawable(animatedVector)
                            animatedVector.start()
                            binding.statusText.text = getString(R.string.sherlock_script_error_4)
                        }

                        SherlockStatus.WARNING_BUILD_NUMBER_MONTH -> {
                            val animatedVector = ContextCompat.getDrawable(
                                this@SherlockActivity,
                                R.drawable.ic_sherlock_warning_face
                            ) as AnimatedVectorDrawable
                            binding.statusImage.setImageDrawable(animatedVector)
                            animatedVector.start()
                            binding.statusText.text = getString(R.string.sherlock_script_error_5)
                        }

                        SherlockStatus.WARNING_BUILD_NUMBER_REVISION -> {
                            val animatedVector = ContextCompat.getDrawable(
                                this@SherlockActivity,
                                R.drawable.ic_sherlock_warning_face
                            ) as AnimatedVectorDrawable
                            binding.statusImage.setImageDrawable(animatedVector)
                            animatedVector.start()
                            binding.statusText.text = getString(R.string.sherlock_script_error_6)
                        }
                    }
                }
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, SherlockManualFragment()).commit()

        binding.tabManual.setOnClickListener {
            binding.tabManual.setTabSelected(true)
            binding.tabScript.setTabSelected(false)
            binding.statusText.text = getString(R.string.sherlock_manual_description)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SherlockManualFragment()).commit()
            sherlockViewModel.setSherlockStatus(SherlockStatus.INITIAL)
        }

        binding.tabScript.setOnClickListener {
            binding.tabManual.setTabSelected(false)
            binding.tabScript.setTabSelected(true)
            binding.statusText.text = getString(R.string.sherlock_script_description)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SherlockScriptFragment()).commit()
            sherlockViewModel.setSherlockStatus(SherlockStatus.INITIAL)
        }
    }
}