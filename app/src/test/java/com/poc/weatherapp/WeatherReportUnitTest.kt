package com.poc.weatherapp

import android.content.Context
import com.poc.weatherapp.viewmodel.WeatherViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.CountDownLatch


class WeatherReportUnitTest {

    private lateinit var context: Context
    private lateinit var viewModel: WeatherViewModel

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        context = mock(Context::class.java)
        viewModel = WeatherViewModel(context)

    }

    /**
     * To test the current weather
     */
    @Test
    fun testWeatherReport() {
        val signal = CountDownLatch(1)
        viewModel.getWeatherDataUT(12.9514147, 80.2436142)
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
