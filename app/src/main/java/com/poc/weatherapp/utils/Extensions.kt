package com.poc.weatherapp.utils

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar


fun AppCompatActivity.showSnackbar(
    mainTextStringId: Int, actionStringId: Int,
    listener: View.OnClickListener
) {
    Snackbar.make(
        findViewById<View>(android.R.id.content),
        getString(mainTextStringId),
        Snackbar.LENGTH_INDEFINITE
    )
        .setAction(getString(actionStringId), listener).show()
}

