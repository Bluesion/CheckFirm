package com.illusion.checkfirm.features.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.illusion.checkfirm.BuildConfig
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmActivity
import com.illusion.checkfirm.common.util.Tools
import com.illusion.checkfirm.databinding.ActivitySettingsBinding
import com.illusion.checkfirm.features.catcher.ui.InfoCatcherActivity
import com.illusion.checkfirm.features.settings.about.AboutActivity
import com.illusion.checkfirm.features.settings.backuprestore.BackupRestoreActivity
import com.illusion.checkfirm.features.settings.bookmark.BookmarkOrderDialog
import com.illusion.checkfirm.features.settings.bookmark.BookmarkResetDialog
import com.illusion.checkfirm.features.settings.help.HelpActivity
import com.illusion.checkfirm.features.settings.language.LanguageDialog
import com.illusion.checkfirm.features.settings.profile.ProfileDialog
import com.illusion.checkfirm.features.settings.theme.ThemeDialog
import com.illusion.checkfirm.features.settings.viewmodel.SettingsViewModel
import com.illusion.checkfirm.features.welcome.ui.WelcomeSearchActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsActivity : CheckFirmActivity<ActivitySettingsBinding>(),
    CompoundButton.OnCheckedChangeListener {

    private val settingsViewModel by viewModels<SettingsViewModel>()

    override fun createBinding() = ActivitySettingsBinding.inflate(layoutInflater)

    override fun setContentInset() {
        setBottomInset(binding.main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(binding.includeToolbar.appBar, getString(R.string.settings))

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingsViewModel.settingsState.collect {
                    binding.userName.text = it.profileName
                    binding.quickSearchBarSwitch.isChecked = it.isQuickSearchBarEnabled
                    binding.welcomeSearchSwitch.isChecked = it.isWelcomeSearchEnabled
                    binding.infoCatcherSwitch.isChecked = it.isInfoCatcherEnabled
                    binding.firebaseSwitch.isChecked = !it.isFirebaseEnabled
                }
            }
        }

        binding.cardProfile.setOnClickListener {
            ProfileDialog(
                currentName = settingsViewModel.settingsState.value.profileName,
                onNameChanged = { name ->
                    settingsViewModel.setProfileName(name)
                }
            ).show(supportFragmentManager, null)
        }

        binding.settingsTheme.setOnClickListener {
            ThemeDialog().show(supportFragmentManager, null)
        }

        binding.settingsLanguage.setOnClickListener {
            LanguageDialog().show(supportFragmentManager, null)
        }

        binding.settingsBookmarkOrder.setOnClickListener {
            BookmarkOrderDialog().show(supportFragmentManager, null)
        }

        binding.settingsBookmarkReset.setOnClickListener {
            BookmarkResetDialog().show(supportFragmentManager, null)
        }

        binding.settingsBookmarkBackupRestore.setOnClickListener {
            startActivity(Intent(this, BackupRestoreActivity::class.java))
        }

        binding.settingsQuickSearchBar.setOnClickListener {
            binding.quickSearchBarSwitch.toggle()
        }

        binding.settingsWelcomeSearch.setOnClickListener {
            startActivity(Intent(this, WelcomeSearchActivity::class.java))
        }

        binding.settingsInfoCatcher.setOnClickListener {
            startActivity(Intent(this, InfoCatcherActivity::class.java))
        }

        binding.settingsFirebase.setOnClickListener {
            binding.firebaseSwitch.toggle()
        }

        binding.settingsHelp.setOnClickListener {
            startActivity(Intent(this, HelpActivity::class.java))
        }

        binding.settingsAbout.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        binding.settingsInquiry.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_SEND).apply {
                    type = "plain/text"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("checkfirmhelpdesk@gmail.com"))
                    putExtra(Intent.EXTRA_SUBJECT, "")
                    putExtra(Intent.EXTRA_TEXT, getDefaultEmailBody())
                })
            } catch (_: ActivityNotFoundException) {
                Toast.makeText(
                    this,
                    getString(R.string.settings_inquiry_not_found_exception),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.quickSearchBarSwitch.setOnCheckedChangeListener(this)
        binding.welcomeSearchSwitch.setOnCheckedChangeListener(this)
        binding.infoCatcherSwitch.setOnCheckedChangeListener(this)
        binding.firebaseSwitch.setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(p0: CompoundButton, isChecked: Boolean) {
        when (p0.id) {
            R.id.quick_search_bar_switch -> {
                settingsViewModel.enableQuickSearchBar(isChecked)
            }

            R.id.welcome_search_switch -> {
                settingsViewModel.enableWelcomeSearch(isChecked)
            }

            R.id.info_catcher_switch -> {
                settingsViewModel.enableInfoCatcher(isChecked)
            }

            R.id.firebase_switch -> {
                settingsViewModel.enableFirebase(!isChecked)
            }
        }
    }

    private fun getDefaultEmailBody(): String {
        return "\n" +
                "\n" +
                "\n" +
                "\n" +
                "*****\n" +
                "App version: ${BuildConfig.VERSION_NAME}\n" +
                "Android version: ${Build.VERSION.RELEASE}\n" +
                "Device: ${Build.MODEL} (${Tools.getCSC()})\n" +
                "*****\n" +
                "\n"
    }
}