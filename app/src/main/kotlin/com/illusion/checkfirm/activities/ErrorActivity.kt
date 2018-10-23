package com.illusion.checkfirm.activities

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.illusion.checkfirm.R
import com.illusion.checkfirm.utils.ExceptionHandler
import com.illusion.checkfirm.utils.GMailSender
import com.illusion.checkfirm.utils.ThemeChanger
import com.illusion.checkfirm.utils.Tools
import java.lang.ref.WeakReference

class ErrorActivity : AppCompatActivity() {

    private lateinit var brandString: String
    private lateinit var modelString: String
    private var sdkInt: Int = 0
    private lateinit var versionString: String
    private lateinit var errorString: String
    private lateinit var descriptionString : String
    private lateinit var emailString: String
    private lateinit var description: TextInputEditText
    private lateinit var email: TextInputEditText

    private val handler = MyHandler(this@ErrorActivity)
    private class MyHandler(activity: ErrorActivity): Handler() {
        private val mActivity: WeakReference<ErrorActivity> = WeakReference(activity)

        override fun handleMessage(msg: Message) {
            val activity = mActivity.get()
            activity?.handleMessage()
        }
    }

    private fun handleMessage() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))
        ThemeChanger.setAppTheme(this)
        setContentView(R.layout.activity_error)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        description = findViewById(R.id.description)
        email = findViewById(R.id.email)

        brandString = intent.getStringExtra("brand")
        modelString = intent.getStringExtra("model")
        sdkInt = intent.getIntExtra("sdk", Build.VERSION.SDK_INT)
        versionString = intent.getStringExtra("version")
        errorString = intent.getStringExtra("error")

        val reportButton = findViewById<MaterialButton>(R.id.report)
        reportButton.setOnClickListener {
            if (Tools.isOnline(applicationContext)) {
                sendError()
                Tools.restart(this, 0)
            } else {
                Toast.makeText(applicationContext, R.string.check_network, Toast.LENGTH_SHORT).show()
            }
        }

        val restartButton = findViewById<MaterialButton>(R.id.restart)
        restartButton.setOnClickListener {
            Tools.restart(this, 0)
        }
    }

    private fun sendError() {
        val mHandler = MyHandler(this@ErrorActivity)
        object : Thread() {
            override fun run() {
                mHandler.post {}
                try {
                    emailString = email.toString()
                    descriptionString = description.toString()

                    val message = "브랜드:\n" + brandString +
                            "\n" +
                            "\n" +
                            "모델:\n" +
                            modelString+
                            "\n" +
                            "\n" +
                            "SDK 버전:\n" +
                            sdkInt.toString() +
                            "\n" +
                            "\n" +
                            "앱 버전:\n" +
                            versionString +
                            "\n" +
                            "\n" +
                            "에러 내용:\n" +
                            errorString +
                            "\n" +
                            "\n" +
                            "유저 설명:\n" +
                            descriptionString +
                            "\n" +
                            "\n" +
                            "답변받을 이메일:\n" +
                            emailString
                    val sender = GMailSender("illusionkernel@gmail.com", "illusion")
                    try {
                        sender.sendMail("CheckFirm 애플리케이션", message, "test@gmail.com", "dnjscjf098@gmail.com")
                    } catch (ignored: Exception) {}

                } catch (ignored: Exception) {}

                mHandler.post {
                    Toast.makeText(applicationContext, R.string.toast, Toast.LENGTH_SHORT).show()
                    handler.sendEmptyMessage(0)
                }
            }
        }.start()
    }
}