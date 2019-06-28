package com.example.airport20

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
}