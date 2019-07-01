package com.example.airport20.utils

import java.time.format.DateTimeFormatter

fun parseTime(time: String): String {
    val formatter = DateTimeFormatter.ofPattern("hh:mm")
    return time.format(formatter)
}