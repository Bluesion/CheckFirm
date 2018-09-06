package com.illusion.checkfirm.activities

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.illusion.checkfirm.R
import com.illusion.checkfirm.utils.ExceptionHandler
import com.illusion.checkfirm.utils.GMailSender
import java.lang.ref.WeakReference

class ErrorActivity : AppCompatActivity() {

    private lateinit var brandString: String
    private lateinit var modelString: String
    private var sdkInt: Int = 0
    private lateinit var versionString: String
    private lateinit var errorString: String

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
        setContentView(R.layout.activity_error)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        brandString = intent.getStringExtra("brand")
        modelString = intent.getStringExtra("model")
        sdkInt = intent.getIntExtra("sdk", Build.VERSION.SDK_INT)
        versionString = intent.getStringExtra("version")
        errorString = intent.getStringExtra("error")

        val reportButton = findViewById<Button>(R.id.report)
        reportButton.setOnClickListener {
            if (MainActivity.isOnline(applicationContext)) {
                sendError()
                restart(this, 0)
            } else {
                Toast.makeText(applicationContext, "네트워크를 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        val restartButton = findViewById<Button>(R.id.restart)
        restartButton.setOnClickListener {
            restart(this, 0)
        }

        val brand = findViewById<TextView>(R.id.brand)
        brand.text = brandString

        val model = findViewById<TextView>(R.id.model)
        model.text = modelString

        val sdk = findViewById<TextView>(R.id.sdk)
        sdk.text = sdkInt.toString()

        val version = findViewById<TextView>(R.id.version)
        version.text = versionString

        val error = findViewById<TextView>(R.id.error)
        error.text = errorString
        error.movementMethod
    }

    override fun onBackPressed() {}

    private fun sendError() {
        val mHandler = MyHandler(this@ErrorActivity)
        object : Thread() {
            override fun run() {
                mHandler.post {}
                try {
                    val message = "제조사:\n" + brandString +
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
                            errorString
                    val sender = GMailSender("illusionkernel@gmail.com", "illusion")
                    try {
                        sender.sendMail("CheckFirm 애플리케이션", message, "test@gmail.com", "dnjscjf098@gmail.com")
                    } catch (ignored: Exception) {}

                } catch (ignored: Exception) {}

                mHandler.post {
                    Toast.makeText(applicationContext, "정상적으로 오류가 전송되었습니다.", Toast.LENGTH_SHORT).show()
                    handler.sendEmptyMessage(0)
                }
            }
        }.start()
    }

    private fun restart(activity: Activity, delay: Int) {
        var delay = delay
        if (delay == 0) {
            delay = 1
        }
        val restartIntent = activity.packageManager.getLaunchIntentForPackage(activity.packageName)
        restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        val intent = PendingIntent.getActivity(activity, 0, restartIntent, PendingIntent.FLAG_ONE_SHOT)
        val manager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.set(AlarmManager.RTC, System.currentTimeMillis() + delay, intent)
        activity.setResult(Activity.RESULT_CANCELED)
        activity.finishAffinity()
    }
}