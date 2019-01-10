package com.poc.weatherapp.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

private const val EXCEPTION = "EXCEPTION"

class BaseUtils {
    companion object {
        fun triggerApplicationSettings(mContext: Context, mPackageName: String) {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.data = Uri.parse("package:$mPackageName")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            mContext.startActivity(intent)
        }

        fun isNetworkConnected(mContext: Context): Boolean {
            var connected = false
            try {
                val connectivityManager =
                    mContext.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val nInfo = connectivityManager.activeNetworkInfo
                connected = nInfo != null && nInfo.isAvailable && nInfo.isConnected
                return connected
            } catch (e: Exception) {
                Log.e(EXCEPTION, e.message)
            }
            return connected
        }

        fun getDate(milliseconds: Long, pattern: String): String {
            val date = Date(milliseconds * 1000L)
            val formatter = SimpleDateFormat(pattern, Locale.getDefault())
            return formatter.format(date)
        }

    }
}