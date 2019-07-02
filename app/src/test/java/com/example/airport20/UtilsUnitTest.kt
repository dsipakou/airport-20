package com.example.airport20

import com.example.airport20.utils.parseTime
import com.example.airport20.utils.sanitizeString
import junit.framework.Assert.assertEquals
import org.junit.Test

class UtilsUnitTest {
    @Test
    fun string_utils_isCorrect() {
        val inputString = "Hello -3 world"
        val outputString = sanitizeString(inputString)
        assertEquals("hello3world", outputString)
    }

    @Test
    fun date_utils_isCorrect() {
        val inputString = "30.06 03:15"
        val outputString = parseTime(inputString)
        assertEquals("30.06", outputString.date)
        assertEquals("03:15", outputString.time)
    }

    @Test
    fun date_utils_timeOnly() {
        val inputString = "03:15"
        val outputString = parseTime(inputString)
        assertEquals("", outputString.date)
        assertEquals("03:15", outputString.time)
    }
}