package com.example.airport20.domain

import android.util.Log
import com.example.airport20.R

enum class Status(val item: Int) {
    AIRBORNE(R.string.airborne),
    ARRIVED(R.string.arrived),
    CANCELED(R.string.canceled),
    DELAYED(R.string.delayed),
    BOARDING(R.string.boarding),
    BOARDING_FINISHED(R.string.boarding_finished),
    LANDED(R.string.landed),
    LEAVING(R.string.leaving),
    CHECKIN(R.string.checkin),
    CHECKINCLOSED(R.string.checkinclosed),
    ENROUTE(R.string.enroute),
    UNKNOWN(-1),
    EMPTY(-1);

    companion object {
        fun fromString(type: String) : Status {
            try {
                when (type) {
                    "" -> return EMPTY
                    "EN ROUTE" -> return ENROUTE
                    "CHECK-IN" -> return CHECKIN
                    "BOARDING FINISHED" -> return BOARDING_FINISHED
                    "CHECK-IN CLOSED" -> return CHECKINCLOSED
                    else -> return valueOf(type.toUpperCase())
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
