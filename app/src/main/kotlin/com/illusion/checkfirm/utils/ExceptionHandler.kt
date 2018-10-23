package com.illusion.checkfirm.utils

import android.app.Activity
import android.content.Intent
import android.os.Build
import com.illusion.checkfirm.BuildConfig
import com.illusion.checkfirm.activities.ErrorActivity
import java.io.PrintWriter
import java.io.StringWriter

class ExceptionHandler internal constructor(private val myContext: Activity) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        val stackTrace = StringWriter()
        exception.printStackTrace(PrintWriter(stackTrace))
        val brand = Build.BRAND
        val model = Build.MODEL
        val sdk = Build.VERSION.SDK_INT
        val version = BuildConfig.VERSION_NAME
        val error = stackTrace.toString()

        val intent = Intent(myContext, ErrorActivity::class.java)
        intent.putExtra("brand", brand)
        intent.putExtra("model", model)
        intent.putExtra("sdk", sdk)
        intent.putExtra("version", version)
        intent.putExtra("error", error)
        myContext.startActivity(intent)

        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(10)
    }
}