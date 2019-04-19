package com.example.airport20.domain

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
    ENROUTE
}

enum class FlightType(val type: Int) {
    ARRIVAL(0),
    DEPARTURE(1);

    companion object {
        private val map = FlightType.values().associateBy(FlightType::type)
        fun fromInt(type: Int) = map[type]
    }
}