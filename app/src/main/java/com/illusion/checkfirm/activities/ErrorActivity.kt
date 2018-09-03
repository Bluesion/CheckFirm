package com.illusion.checkfirm.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.illusion.checkfirm.utils.ExceptionHandler
import java.util.Objects

class ErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))
        Toast.makeText(applicationContext, "기기명과 국가코드를 정확히 입력해주세요", Toast.LENGTH_SHORT).show()
        restart(this, 0)
    }

    companion object {
        fun restart(activity: Activity, delay: Int) {
            var delay = delay
            if (delay == 0) {
                delay = 1
            }
            val restartIntent = activity.packageManager.getLaunchIntentForPackage(activity.packageName)
            restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            val intent = PendingIntent.getActivity(activity, 0, restartIntent, PendingIntent.FLAG_ONE_SHOT)
            val manager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            Objects.requireNonNull(manager).set(AlarmManager.RTC, System.currentTimeMillis() + delay, intent)
            activity.setResult(Activity.RESULT_CANCELED)
            activity.finishAffinity()
        }
    }
}
