package com.illusion.checkfirm.features.main.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmActivity
import com.illusion.checkfirm.databinding.ActivityReportBinding
import com.illusion.checkfirm.common.ui.LoadingDialog
import jakarta.mail.Authenticator
import jakarta.mail.Message
import jakarta.mail.PasswordAuthentication
import jakarta.mail.Session
import jakarta.mail.Transport
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeBodyPart
import jakarta.mail.internet.MimeMessage
import jakarta.mail.internet.MimeMultipart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Properties

class ReportActivity : CheckFirmActivity<ActivityReportBinding>() {

    private lateinit var loadingDialog: LoadingDialog

    override fun createBinding() = ActivityReportBinding.inflate(layoutInflater)
    override fun setContentInset() {
        setBottomInset(binding.main)
        setHorizontalInset(binding.main, 12)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(binding.includeToolbar.appBar, getString(R.string.report))

        binding.report1Layout.setOnClickListener {
            binding.report1.toggle()
        }

        binding.report2Layout.setOnClickListener {
            binding.report2.toggle()
        }

        binding.report3Layout.setOnClickListener {
            binding.report3.toggle()
        }

        binding.report4Layout.setOnClickListener {
            binding.report4.toggle()
        }

        loadingDialog = LoadingDialog(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_report, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.submit -> {
                if (!loadingDialog.isShowing) {
                    loadingDialog.show()
                }
                lifecycleScope.launch(Dispatchers.IO) {
                    submit(intent.getIntExtra("index", 0))
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private suspend fun submit(i: Int) {
        var isSuccess = true
        try {
            val props = Properties().apply {
                this["mail.smtp.host"] = "smtp.gmail.com"
                this["mail.smtp.socketFactory.port"] = "465"
                this["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
                this["mail.smtp.auth"] = "true"
                this["mail.smtp.port"] = "465"
            }

            val session = Session.getDefaultInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication() =
                    PasswordAuthentication("checkfirmhelpdesk@gmail.com", "vzmkrotliewaifaf")
            })

            val mail = MimeMessage(session).apply {
                setFrom(InternetAddress("checkfirmhelpdesk@gmail.com"))
                addRecipient(
                    Message.RecipientType.TO, InternetAddress("checkfirmhelpdesk@gmail.com")
                )
                subject = "[신고] ${CheckFirm.searchModel[i]} (${CheckFirm.searchCSC[i]})"
            }

            var message = "[오류 내용]"
            val messageBodyPart = MimeBodyPart()

            if (binding.report1.isChecked) {
                message += "<br>- 펌웨어 정보 오류"
            }

            if (binding.report2.isChecked) {
                message += "<br>- 부적절한 셜록 닉네임"
            }

            if (binding.report3.isChecked) {
                message += "<br>- 스마트 서치 정보 오류"
            }

            if (binding.report4.isChecked) {
                message += "<br>- 기타 오류"
            }

            message += "<br><br>[유저 메시지]<br>"
            message += if (binding.detailText.text.isNullOrBlank()) {
                "메시지 없음"
            } else {
                binding.detailText.text.toString()
            }

            messageBodyPart.setText(message, "utf-8", "html")

            mail.setContent(MimeMultipart().apply {
                addBodyPart(messageBodyPart)
            })

            Transport.send(mail)
        } catch (e1: Exception) {
            isSuccess = false
            e1.printStackTrace()
        } catch (e2: Error) {
            isSuccess = false
            e2.printStackTrace()
        } finally {
            withContext(Dispatchers.Main) {
                loadingDialog.dismiss()
                if (isSuccess) {
                    Toast.makeText(this@ReportActivity, getString(R.string.report_success), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ReportActivity, getString(R.string.report_fail), Toast.LENGTH_SHORT).show()
                }
                finish()
            }
        }
    }
}