package com.example.airport20.utils

import com.example.airport20.domain.AirportTime

fun parseTime(time: String): AirportTime {
    val re = Regex("(?:(\\d+.\\d+) )?(\\d+:\\d+)\$")
    val formatter = re.find(time)
    val airportTime: AirportTime
    if (formatter?.groups?.size!! > 2) {
        airportTime = AirportTime(formatter.groups[1].toString(), formatter.groups[2].toString())
    } else {
        airportTime = AirportTime("", formatter.groups[1].toString())
    }
    return airportTime
}