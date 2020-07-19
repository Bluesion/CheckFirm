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
import com.google.android.material.textview.MaterialTextView
import com.illusion.checkfirm.R
import com.illusion.checkfirm.databinding.ActivitySherlockBinding
import org.spongycastle.crypto.digests.MD5Digest
import org.spongycastle.util.encoders.Hex
import java.nio.charset.StandardCharsets

class SherlockActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySherlockBinding
    private var buildPrefix = ""
    private var cscPrefix = ""
    private var basebandPrefix = ""
    private var userFirmware = ""

    private lateinit var buildEditor: TextInputEditText
    private lateinit var cscEditor: TextInputEditText
    private lateinit var basebandEditor: TextInputEditText
    private lateinit var samsungEncryptedText: MaterialTextView
    private lateinit var userEncryptedText: MaterialTextView
    private lateinit var status: MaterialTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySherlockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()

        val officialFirmware = intent.getStringExtra("official")!!

        val firstIndex = officialFirmware.indexOf("/")
        val secondIndex = officialFirmware.lastIndexOf("/")

        val buildVersion = officialFirmware.substring(0, firstIndex)
        val cscVersion = officialFirmware.substring(firstIndex + 1, secondIndex)
        var basebandVersion = ""
        var basebandInfo = ""

        val length = buildVersion.length
        buildPrefix = buildVersion.substring(0, length - 6)
        cscPrefix = cscVersion.substring(0, length - 5)
        if (officialFirmware.length - 1 > secondIndex) {
            basebandVersion = officialFirmware.substring(secondIndex + 1)
            basebandPrefix = basebandVersion.substring(0, length - 6)
            basebandInfo = basebandVersion.substring(length - 6)
        }

        val buildInfo = buildVersion.substring(length - 6)
        val cscInfo = cscVersion.substring(length - 5)

        binding.textFieldBuild.prefixText = buildPrefix
        buildEditor = binding.editorBuild
        buildEditor.setText(buildInfo)
        buildEditor.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                compare()
            }
        })

        binding.textFieldCsc.prefixText = cscPrefix
        cscEditor = binding.editorCsc
        cscEditor.setText(cscInfo)
        cscEditor.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                compare()
            }
        })

        binding.textFieldBaseband.prefixText = basebandPrefix
        basebandEditor = binding.editorBaseband
        basebandEditor.setText(basebandInfo)
        basebandEditor.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                compare()
            }
        })

        samsungEncryptedText = binding.samsungEncryptedText
        samsungEncryptedText.text = intent.getStringExtra("firmware")

        userFirmware = buildPrefix + buildEditor.text.toString() + "/" +
                cscPrefix + cscEditor.text.toString() + "/" + basebandPrefix + basebandEditor.text.toString()
        binding.userText.text = userFirmware

        userEncryptedText = binding.userEncryptedText
        userEncryptedText.text = getMD5(userFirmware.toByteArray(StandardCharsets.UTF_8))

        status = binding.status
        if (samsungEncryptedText.text == userEncryptedText.text) {
            status.text = getString(R.string.sherlock_correct)
            status.setTextColor(resources.getColor(R.color.green, theme))
        } else {
            status.text = getString(R.string.sherlock_not_correct)
            status.setTextColor(resources.getColor(R.color.red, theme))
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
        binding.userText.text = userFirmware
        userEncryptedText.text = getMD5(userFirmware.toByteArray(StandardCharsets.UTF_8))

        if (samsungEncryptedText.text == userEncryptedText.text) {
            status.text = getString(R.string.sherlock_correct)
            status.setTextColor(resources.getColor(R.color.green, theme))
        } else {
            status.text = getString(R.string.sherlock_not_correct)
            status.setTextColor(resources.getColor(R.color.red, theme))
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.includeToolbar.toolbar)
        val toolbarText = getString(R.string.sherlock)

        val title = binding.includeToolbar.title
        val expandedTitle = binding.includeToolbar.expandedTitle
        title.text = toolbarText
        expandedTitle.text = toolbarText

        val mAppBar = binding.includeToolbar.appbar
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