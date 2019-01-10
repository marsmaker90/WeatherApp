package com.poc.weatherapp

import com.poc.weatherapp.utils.BaseUtils
import org.junit.Assert.assertEquals
import org.junit.Test


class UtilsUnitTest {
    @Test
    fun validDateFormat() {
        val milliseconds: Long = 1547064455
        assertEquals("Invalid date format", "10-Jan-2019", BaseUtils.getDate(milliseconds, BaseConfig.FORMAT_DATE))
    }

    @Test
    fun validTimeHoursFormat() {
        val milliseconds: Long = 1547062200
        assertEquals("Invalid time format", "1AM", BaseUtils.getDate(milliseconds, BaseConfig.FORMAT_HOURS))
    }

    @Test
    fun validTimeFormat() {
        val milliseconds: Long = 1547062200
        assertEquals("Invalid time format", "01:00:00", BaseUtils.getDate(milliseconds, BaseConfig.FORMAT_TIME))
    }
}
