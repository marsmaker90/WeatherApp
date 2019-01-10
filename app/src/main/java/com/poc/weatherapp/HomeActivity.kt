package com.poc.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.poc.weatherapp.adapter.WeatherDataAdapter
import com.poc.weatherapp.model.WeatherData
import com.poc.weatherapp.utils.BaseUtils
import com.poc.weatherapp.utils.ProgressDialog
import com.poc.weatherapp.utils.showSnackbar
import com.poc.weatherapp.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_main.*
import java.text.DateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: WeatherViewModel

    // Attributes to Fetch the current location details using the Fused location client
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mSettingsClient: SettingsClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    private var mLocationCallback: LocationCallback? = null
    private var mCurrentLocation: Location? = null
    private var mRequestingLocationUpdates: Boolean = true
    private var mPermissionNeverAskAgain: Boolean = false
    private var mLastUpdateTime: String? = null

    private lateinit var adapter: WeatherDataAdapter

    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewModel = WeatherViewModel(this)
        progressDialog = ProgressDialog(this)
        mLastUpdateTime = ""

        setSupportActionBar(toolbar)

        hourlyWeatherListView.setHasFixedSize(true)
        hourlyWeatherListView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = WeatherDataAdapter(this)
        hourlyWeatherListView.adapter = adapter

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mSettingsClient = LocationServices.getSettingsClient(this)

        createLocationCallback()
        createLocationRequest()
        buildLocationSettingsRequest()

        fab.setOnClickListener {
            startLocationUpdates()
        }
    }

    private fun updateValuesFromBundle(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING)
            }
            getWeatherReport()
        }
    }


    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest?.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest?.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }


    private fun createLocationCallback() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                mCurrentLocation = locationResult.lastLocation
                mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
                getWeatherReport()
            }
        }
    }

    private fun buildLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest ?: LocationRequest())
        mLocationSettingsRequest = builder.build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.i(TAG, getString(R.string.user_agreed_location_settings_changes))
                    mRequestingLocationUpdates = true
                }
                Activity.RESULT_CANCELED -> {
                    Log.i(TAG, getString(R.string.user_denied_location_settings_changes))
                    mRequestingLocationUpdates = false
                }
            }
        }
    }

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient?.checkLocationSettings(mLocationSettingsRequest)
            ?.addOnSuccessListener(this) {
                Log.i(TAG, getString(R.string.location_settings_are_satisfied))
                mFusedLocationClient?.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback, Looper.myLooper()
                )

            }
            ?.addOnFailureListener(this) { e ->
                val statusCode = (e as ApiException).statusCode
                when (statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.i(TAG, getString(R.string.location_settings_are_not_satisfied))
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(this@HomeActivity, REQUEST_CHECK_SETTINGS)
                        } catch (sie: IntentSender.SendIntentException) {
                            Log.i(TAG, getString(R.string.unable_to_sent_request))
                        }

                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        Log.e(TAG, getString(R.string.location_settings_are_inadequate))
                        Toast.makeText(
                            this@HomeActivity,
                            getString(R.string.location_settings_are_inadequate),
                            Toast.LENGTH_LONG
                        ).show()
                        mRequestingLocationUpdates = false
                    }
                }
            }
    }

    private fun stopLocationUpdates() {
        if ((!mRequestingLocationUpdates)) {
            Log.d(TAG, getString(R.string.stop_location_update))
            return
        }

        mFusedLocationClient?.removeLocationUpdates(mLocationCallback)
            ?.addOnCompleteListener(this) {
                mRequestingLocationUpdates = false
            }
    }

    override fun onStart() {
        mRequestingLocationUpdates = true
        super.onStart()
    }

    public override fun onResume() {
        super.onResume()
        if (checkPermissions()) {
            startLocationUpdates()
        } else if (!checkPermissions() && !mPermissionNeverAskAgain) {
            requestPermissions()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    /**
     * Stores activity data in the Bundle.
     */
    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putParcelable(KEY_LOCATION, mCurrentLocation)
        outState?.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime)
        super.onSaveInstanceState(outState)
    }


    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (shouldProvideRationale) {
            Log.i(TAG, getString(R.string.display_permission_rationale))
            showSnackbar(R.string.permission_rationale,
                android.R.string.ok, View.OnClickListener {
                    ActivityCompat.requestPermissions(
                        this@HomeActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_PERMISSIONS_REQUEST_CODE
                    )
                })
        } else {
            Log.i(TAG, getString(R.string.requesting_permission))
            ActivityCompat.requestPermissions(
                this@HomeActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.i(TAG, getString(R.string.on_request_permission_result))
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (!grantResults.isEmpty()) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                    // Permission denied.

                    showSnackbar(R.string.permission_rationale,
                        R.string.settings, View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            BaseUtils.triggerApplicationSettings(this, packageName)
                        })
                } else {
                    if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                        //allowed
                        startLocationUpdates()
                    } else {
                        //set to never ask again
                        mPermissionNeverAskAgain = true
                        showSnackbar(R.string.permission_rationale,
                            R.string.settings, View.OnClickListener {
                                // Build intent that displays the App settings screen.
                                BaseUtils.triggerApplicationSettings(this, packageName)

                                mPermissionNeverAskAgain = false
                            })

                    }

                }
            } else {
                // receive empty arrays.
                Log.i(TAG, getString(R.string.user_interaction_was_cancelled))
            }
        }
    }


    /**
     * Method to fetch the current weather report
     */
    private fun getWeatherReport() {
        if (BaseUtils.isNetworkConnected(this)) {
            if (mCurrentLocation != null) {
                showProgress()
                viewModel.compositeDisposable.add(
                    viewModel.getWeatherData(mCurrentLocation?.latitude ?: 0.0, mCurrentLocation?.longitude ?: 0.0)
                        .subscribe({
                            dismissProgress()
                            setCurrentWeatherUI(it)
                            if (it.hourly.data.isNotEmpty()) {
                                adapter.setItem(it.hourly.data)
                            }
                        }, {
                            dismissProgress()
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        })
                )
            }
        } else {
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
        }

    }

    private fun setCurrentWeatherUI(weatherData: WeatherData) {
        val current = weatherData.currently

        val currentWeather =
            getString(R.string.current_weather_with_summary, current.temperature.toInt(), current.summary)
        txtCurrentWeather.text = currentWeather
        txtCurrentWeatherMsg.text = BaseUtils.getDate(current.time, BaseConfig.FORMAT_DATE)
        txtCurrentWeatherTime.text = BaseUtils.getDate(current.time, BaseConfig.FORMAT_TIME)

        setWeatherDrawable(txtCurrentWeather, current.icon)

        txtWind.text = getString(R.string.wind, current.windSpeed)
        txtHumidity.text = getString(R.string.humidity, current.humidity)
        txtDew.text = getString(R.string.dew, current.dewPoint)
        txtPressure.text = getString(R.string.pressure, current.pressure)
        txtVisible.text = getString(R.string.visibility, current.visibility)
        txtUI.text = getString(R.string.uv_index, current.uvIndex)
    }

    private fun setWeatherDrawable(textView: TextView, type: String) {
        when (type) {
            getString(R.string.clear_day) -> textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.clear, 0, 0, 0)
            getString(R.string.clear_night) -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.night,
                0,
                0,
                0
            )
            getString(R.string.partly_cloudy_day) -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.partly_cloudy,
                0,
                0,
                0
            )
            getString(R.string.cloudy_night) -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.night_cloudy,
                0,
                0,
                0
            )
            getString(R.string.cloudy) -> textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cloudy, 0, 0, 0)
            getString(R.string.rain) -> textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rain, 0, 0, 0)
            getString(R.string.sleet) -> textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sleet, 0, 0, 0)
            getString(R.string.snow) -> textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.snow, 0, 0, 0)
            getString(R.string.wind_icon) -> textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.wind, 0, 0, 0)
            getString(R.string.fog) -> textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.foggy, 0, 0, 0)
        }

    }

    /**
     * show loading progress for complete window
     */

    fun showProgress() {
        if (progressDialog != null && !progressDialog!!.isShowing) {
            progressDialog?.show()
        }
    }

    /**
     * dismiss loading progress
     */

    fun dismissProgress() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog?.dismiss()
        }
    }


    override fun onDestroy() {
        viewModel.onDestroy()
        super.onDestroy()
    }


    companion object {

        private val TAG = HomeActivity::class.java.simpleName
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
        private const val REQUEST_CHECK_SETTINGS = 0x1
        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 180000
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2

        // Keys for storing activity state in the Bundle.
        private const val KEY_LOCATION = "location"
        private const val KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string"

    }


}
