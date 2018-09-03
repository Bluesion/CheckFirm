package com.illusion.checkfirm.utils

import java.io.PrintWriter
import java.io.StringWriter
import android.app.Activity
import android.content.Intent
import android.os.Build
import com.illusion.checkfirm.activities.ErrorActivity

import java.lang.Character.LINE_SEPARATOR

class ExceptionHandler internal constructor(private val myContext: Activity) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        val stackTrace = StringWriter()
        exception.printStackTrace(PrintWriter(stackTrace))
        val errorReport = "************ CAUSE OF ERROR ************\n\n" +
                stackTrace.toString() +
                "\n************ DEVICE INFORMATION ***********\n" +
                "Brand: " +
                Build.BRAND +
                LINE_SEPARATOR +
                "Device: " +
                Build.DEVICE +
                LINE_SEPARATOR +
                "Model: " +
                Build.MODEL +
                LINE_SEPARATOR +
                "Id: " +
                Build.ID +
                LINE_SEPARATOR +
                "Product: " +
                Build.PRODUCT +
                LINE_SEPARATOR +
                "\n************ FIRMWARE ************\n" +
                "SDK: " +
                Build.VERSION.SDK +
                LINE_SEPARATOR +
                "Release: " +
                Build.VERSION.RELEASE +
                LINE_SEPARATOR +
                "Incremental: " +
                Build.VERSION.INCREMENTAL +
                LINE_SEPARATOR

        val intent = Intent(myContext, ErrorActivity::class.java)
        intent.putExtra("error", errorReport)
        myContext.startActivity(intent)

        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(10)
    }

}