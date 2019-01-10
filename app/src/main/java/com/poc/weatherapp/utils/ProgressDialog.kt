package com.poc.weatherapp.utils

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.LinearLayout
import com.poc.weatherapp.R


class ProgressDialog(context: Context) : Dialog(context) {

    init {
        requestWindowFeature(android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        setContentView(R.layout.progress_dialog)
        window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        this.setCancelable(false)
    }


}