package com.example.airport20.utils

import com.example.airport20.domain.AirportTime

fun parseTime(time: String): AirportTime {
    val re = Regex("(?:(\\d+.\\d+) )?(\\d+:\\d+)\$")
    val formatter = re.find(time)
    val airportTime: AirportTime
    airportTime = AirportTime(formatter?.groups?.get(1)?.value, formatter?.groups?.get(2)?.value)
    return airportTime
}