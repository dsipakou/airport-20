package com.example.airport20

import com.example.airport20.utils.sanitizeString
import junit.framework.Assert.assertEquals
import org.junit.Test

class UtilsUnitTest {
    @Test
    fun string_utils_isCorrect() {
        val inputString = "Hello -world"
        val outputString = sanitizeString(inputString)
        assertEquals(outputString, "helloworld")
    }
}