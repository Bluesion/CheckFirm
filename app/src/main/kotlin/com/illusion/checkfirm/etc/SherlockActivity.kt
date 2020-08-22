package com.illusion.checkfirm.etc

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.textfield.TextInputEditText
import com.illusion.checkfirm.R
import com.illusion.checkfirm.databinding.ActivitySherlockBinding
import com.illusion.checkfirm.dialogs.SherlockDialog
import org.spongycastle.crypto.digests.MD5Digest
import org.spongycastle.util.encoders.Hex
import java.nio.charset.StandardCharsets
import java.util.*

class SherlockActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySherlockBinding
    private var buildPrefix = ""
    private var cscPrefix = ""
    private var basebandPrefix = ""
    private var userFirmware = ""
    private var testLatest = ""

    private lateinit var buildEditor: TextInputEditText
    private lateinit var cscEditor: TextInputEditText
    private lateinit var basebandEditor: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySherlockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()

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
            val bottomSheetFragment = SherlockDialog.newInstance(testLatest, getMD5(userFirmware.toByteArray(StandardCharsets.UTF_8))!!)
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

    private fun getMD5(paramArrayOfByte: ByteArray): String? {
        var newText = paramArrayOfByte
        val localMD5Digest = MD5Digest()
        localMD5Digest.update(newText, 0, newText.size)
        newText = ByteArray(localMD5Digest.digestSize)
        localMD5Digest.doFinal(newText, 0)
        return Hex.toHexString(newText)
    }

    private fun compare() {
        userFirmware = buildPrefix + buildEditor.text.toString() + "/" +
            cscPrefix + cscEditor.text.toString() + "/" + basebandPrefix + basebandEditor.text.toString()

        val userFirmwareWithDM = buildPrefix + buildEditor.text.toString() + ".DM/" +
            cscPrefix + cscEditor.text.toString() + "/" + basebandPrefix + basebandEditor.text.toString()

        val encryptedFirmware = getMD5(userFirmware.toByteArray(StandardCharsets.UTF_8))
        val encryptedFirmwareWithDM = getMD5(userFirmwareWithDM.toByteArray(StandardCharsets.UTF_8))

        when (testLatest) {
            encryptedFirmware -> {
                binding.status.text = getString(R.string.sherlock_correct)
                binding.status.setTextColor(resources.getColor(R.color.green, theme))
                binding.userText.text = userFirmware
            }
            encryptedFirmwareWithDM -> {
                binding.editorBuild.setText("${buildEditor.text}.DM")
                binding.status.text = getString(R.string.sherlock_correct)
                binding.status.setTextColor(resources.getColor(R.color.green, theme))
                binding.userText.text = userFirmwareWithDM
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

        val one = getSharedPreferences("settings", Context.MODE_PRIVATE).getBoolean("one", true)
        if (one) {
            mAppBar.setExpanded(true)
        } else {
            mAppBar.setExpanded(false)
        }
    }
}
