package com.poc.weatherapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

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

    }

}