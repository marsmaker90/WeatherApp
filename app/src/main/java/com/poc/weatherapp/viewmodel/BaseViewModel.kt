package com.poc.weatherapp.viewmodel

import android.content.Context
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel(var context: Context) {
    val compositeDisposable = CompositeDisposable()

    /**
     * It must call to Activity Destroy or Fragment DestroyView because we need clear all Rxjava process
     */
    fun onDestroy() {
        if (compositeDisposable.size() > 0 && !compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
            compositeDisposable.clear()
        }
    }
}