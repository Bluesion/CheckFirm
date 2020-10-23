package com.illusion.checkfirm.etc

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.illusion.checkfirm.R
import com.illusion.checkfirm.databinding.ActivitySherlockBinding
import com.illusion.checkfirm.dialogs.SherlockDialog
import com.illusion.checkfirm.utils.Tools
import java.util.*

class SherlockActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySherlockBinding
    private var buildPrefix = ""
    private var cscPrefix = ""
    private var basebandPrefix = ""
    private var userFirmware = ""
    private var testLatest = ""
    private var watson = ""
    private lateinit var settingPrefs: SharedPreferences

    private lateinit var buildEditor: TextInputEditText
    private lateinit var cscEditor: TextInputEditText
    private lateinit var basebandEditor: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySherlockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()

        settingPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        watson = settingPrefs.getString("profile_user_name", "Unknown")!!

        buildEditor = binding.editorBuild
        buildEditor.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                compare()
            }
        })

        cscEditor = binding.editorCsc
        cscEditor.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                compare()
            }
        })

        basebandEditor = binding.editorBaseband
        basebandEditor.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                compare()
            }
        })

        testLatest = intent.getStringExtra("test")!!

        if (!intent.getBooleanExtra("pro_mode", false)) {
            val officialFirmware = intent.getStringExtra("official")!!

            val firstIndex = officialFirmware.indexOf("/")
            val secondIndex = officialFirmware.lastIndexOf("/")

            val buildVersion = officialFirmware.substring(0, firstIndex)
            val cscVersion = officialFirmware.substring(firstIndex + 1, secondIndex)
            var basebandInfo = ""

            val length = buildVersion.length
            buildPrefix = buildVersion.substring(0, length - 6)
            cscPrefix = cscVersion.substring(0, length - 5)
            if (officialFirmware.length - 1 > secondIndex) {
                val basebandVersion = officialFirmware.substring(secondIndex + 1)
                basebandPrefix = basebandVersion.substring(0, length - 6)
                basebandInfo = basebandVersion.substring(length - 6)
            }

            binding.textFieldBuild.prefixText = buildPrefix
            buildEditor.setText(buildVersion.substring(length - 6))

            binding.textFieldCsc.prefixText = cscPrefix
            cscEditor.setText(cscVersion.substring(length - 5))

            binding.textFieldBaseband.prefixText = basebandPrefix
            basebandEditor.setText(basebandInfo)
        } else {
            val dummy = getDate()
            buildEditor.setText(dummy)
            cscEditor.setText(dummy)
            basebandEditor.setText(dummy)
        }

        compare()

        binding.help.setOnClickListener {
            val bottomSheetFragment = SherlockDialog(testLatest, Tools.getMD5Hash(userFirmware)!!)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        binding.copy.setOnClickListener {
            val clip = ClipData.newPlainText("checkfirmSherlock", userFirmware)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, R.string.clipboard, Toast.LENGTH_SHORT).show()
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

    private fun compare() {
        userFirmware = buildPrefix + buildEditor.text.toString() + "/" +
            cscPrefix + cscEditor.text.toString() + "/" + basebandPrefix + basebandEditor.text.toString()

        val userFirmwareWithDM = buildPrefix + buildEditor.text.toString() + ".DM/" +
            cscPrefix + cscEditor.text.toString() + "/" + basebandPrefix + basebandEditor.text.toString()

        val encryptedFirmware = Tools.getMD5Hash(userFirmware)
        val encryptedFirmwareWithDM = Tools.getMD5Hash(userFirmwareWithDM)

        when (testLatest) {
            encryptedFirmware -> {
                binding.status.text = getString(R.string.sherlock_correct)
                binding.status.setTextColor(resources.getColor(R.color.green, theme))
                binding.userText.text = userFirmware
                add(userFirmware)
            }
            encryptedFirmwareWithDM -> {
                binding.editorBuild.setText(String.format(getString(R.string.sherlock_dm_format), buildEditor.text))
                binding.status.text = getString(R.string.sherlock_correct)
                binding.status.setTextColor(resources.getColor(R.color.green, theme))
                binding.userText.text = userFirmwareWithDM
                add(userFirmwareWithDM)
            }
            else -> {
                binding.status.text = getString(R.string.sherlock_not_correct)
                binding.status.setTextColor(resources.getColor(R.color.red, theme))
                binding.userText.text = userFirmware
            }
        }
    }

    private fun getDate(): String {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        val todayYear = calendar.get(Calendar.YEAR)
        val todayMonth = calendar.get(Calendar.MONTH).plus(1)

        val yearSub = todayYear - 2011
        val monthSub = todayMonth - 1

        val year = (75 + yearSub).toChar()
        val month = (65 + monthSub).toChar()

        return "$year$month" + "1"
    }

    private fun initToolbar() {
        setSupportActionBar(binding.includeToolbar.toolbar)
        val toolbarText = getString(R.string.sherlock)

        val title = binding.includeToolbar.title
        val expandedTitle = binding.includeToolbar.expandedTitle
        title.text = toolbarText
        expandedTitle.text = toolbarText

        val mAppBar = binding.includeToolbar.appBar
        mAppBar.layoutParams.height = (resources.displayMetrics.heightPixels * 0.3976).toInt()
        mAppBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, _ ->
            val percentage = (appBarLayout.y / appBarLayout.totalScrollRange)
            expandedTitle.alpha = 1 - (percentage * 2 * -1)
            title.alpha = percentage * -1
        })
    }

    private fun add(decryptedFirmware: String) {
        if (!settingPrefs.getBoolean("firebase", false)) {
            val model = intent.getStringExtra("model")!!
            val csc = intent.getStringExtra("csc")!!
            val db = FirebaseFirestore.getInstance()

            val docRef = db.collection(model).document(csc)
            docRef.update("firmware_decrypted", decryptedFirmware)
            docRef.update("watson", watson)
        }
    }
}
