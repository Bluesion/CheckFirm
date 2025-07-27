package com.illusion.checkfirm.common.util

import android.util.Log
import java.lang.Exception

class Logger {

    companion object {

        fun d() {
            Log.d("CHECKFIRM", logMessage(""))
        }

        fun d(i: Int) {
            Log.d("CHECKFIRM", "#$i")
        }

        fun d(message: String) {
            Log.d("CHECKFIRM", logMessage(message))
        }

        fun s() {
            Log.v("CHECKFIRM", logMessage("START"))
        }

        fun e() {
            Log.v("CHECKFIRM", logMessage("END"))
        }

        fun e(message: String) {
            Log.e("CHECKFIRM", logMessage(message))
        }

        fun e(exception: Exception) {
            Log.e("CHECKFIRM", exception.stackTraceToString())
        }

        fun e(error: Error) {
            Log.e("CHECKFIRM", error.stackTraceToString())
        }

        fun e(message: String, exception: Exception) {
            Log.e("CHECKFIRM", "${logMessage(message)}\n${exception.stackTraceToString()}")
        }

        fun e(message: String, error: Error) {
            Log.e("CHECKFIRM", "${logMessage(message)}\n${error.stackTraceToString()}")
        }

        private fun logMessage(message: String): String {
            val ste = Thread.currentThread().stackTrace
            val className: String
            val methodName: String
            // java
            if (ste[3].methodName == ste[4].methodName) {
                className = ste[5].className
                methodName = ste[5].methodName
            } else { // kotlin
                className = ste[4].className
                methodName = ste[4].methodName
            }

            return StringBuilder().append("[")
                .append(className.substring(className.lastIndexOf(".") + 1))
                .append("::")
                .append(methodName)
                .append("] ")
                .append(message)
                .toString()
        }
    }
}