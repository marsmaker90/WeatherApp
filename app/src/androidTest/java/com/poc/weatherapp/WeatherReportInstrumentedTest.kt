package com.poc.weatherapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.poc.weatherapp.viewmodel.WeatherViewModel
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
class WeatherReportInstrumentedTest {
    @Test
    fun testWeatherReport() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val viewModel = WeatherViewModel(context)
        val signal = CountDownLatch(1)
        viewModel.getWeatherData(12.9514147, 80.2436142)
            .subscribe(
                {
                    Assert.assertNotNull(
                        "Weather Data model communication is success" +
                                "response based on the latitude and longitude", it
                    )
                    signal.countDown()
                },
                {
                    it.message?.isEmpty()?.let { throwable -> Assert.assertTrue("Weather Data Failed", throwable) }
                    signal.countDown()
                })
        signal.await()
    }

}
