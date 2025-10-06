package com.illusion.checkfirm.features.settings.about

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.illusion.checkfirm.BuildConfig
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmActivity
import com.illusion.checkfirm.common.util.Tools
import com.illusion.checkfirm.data.model.remote.ApiResponse
import com.illusion.checkfirm.data.model.remote.AppVersionStatus
import com.illusion.checkfirm.databinding.ActivityAboutBinding
import com.illusion.checkfirm.features.main.viewmodel.AppMetadataViewModel
import com.illusion.checkfirm.features.main.viewmodel.AppMetadataViewModelFactory
import kotlinx.coroutines.launch

class AboutActivity : CheckFirmActivity<ActivityAboutBinding>() {

    private val appMetadataViewModel by viewModels<AppMetadataViewModel> {
        AppMetadataViewModelFactory(
            (application as CheckFirm).repositoryProvider.getMainRepository()
        )
    }

    override fun createBinding() = ActivityAboutBinding.inflate(layoutInflater)

    override fun setContentInset() {
        setBottomInset(binding.license)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(binding.toolbar, "")

        binding.version.text = BuildConfig.VERSION_NAME

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (Tools.isOnline(this@AboutActivity)) {
                    appMetadataViewModel.isOldVersion.collect {
                        when (it) {
                            is ApiResponse.Loading -> {
                                binding.progress.visibility = View.VISIBLE
                                binding.latest.visibility = View.GONE
                                binding.update.visibility = View.GONE
                            }

                            else -> {
                                binding.progress.visibility = View.GONE
                                // I don't care if it's an error, just show the update button
                                if (it is ApiResponse.Success && it.data == AppVersionStatus.LATEST_VERSION) {
                                    binding.latest.visibility = View.VISIBLE
                                    binding.update.visibility = View.GONE
                                } else {
                                    binding.latest.visibility = View.GONE
                                    binding.update.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                } else {
                    binding.progress.visibility = View.GONE
                    binding.latest.text = getString(R.string.check_network)
                    binding.latest.visibility = View.VISIBLE
                    binding.update.visibility = View.GONE
                }
            }
        }

        binding.update.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    "https://play.google.com/store/apps/details?id=${applicationContext.packageName}".toUri()
            })
        }

        binding.contributor.setOnClickListener {
            ContributorDialog().show(supportFragmentManager, null)
        }

        binding.legal.setOnClickListener {
            LegalDialog().show(supportFragmentManager, null)
        }

        binding.license.setOnClickListener {
            LicenseDialog().show(supportFragmentManager, null)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_about, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_information -> {
                startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = "package:$packageName".toUri()
                })
                return true
            }

            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
