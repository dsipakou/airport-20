package com.example.airport20.domain

import android.util.Log

enum class Status {
    AIRBORNE,
    ARRIVED,
    CANCELED,
    DELAYED,
    BOARDING,
    BOARDING_FINISHED,
    LANDED,
    LEAVING,
    CHECKIN,
    CHECKINCLOSED,
    ENROUTE,
    UNKNOWN,
    EMPTY;

    companion object {
        fun fromString(type: String) : Status {
            try {
                when (type) {
                    "" -> return EMPTY
                    "EN ROUTE" -> return ENROUTE
                    "CHECK-IN" -> return CHECKIN
                    "BOARDING FINISHED" -> return BOARDING_FINISHED
                    "CHECK-IN CLOSED" -> return CHECKINCLOSED
                    else -> return valueOf(type)
                }
            }
            catch (e: Exception) {
                Log.e("Status get", e.toString())
                return UNKNOWN
            }
        }
    }
}

enum class FlightType(val type: Int) {
    ARRIVAL(0),
    DEPARTURE(1);

    companion object {
        private val map = FlightType.values().associateBy(FlightType::type)
        fun fromInt(type: Int) = map[type]
    }
}

enum class TimeRange(val time: String) {
    NOW(""),
    YESTERDAY("yesterday"),
    TODAY("today"),
    TOMORROW("tomorrow")
}
