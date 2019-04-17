package com.example.airport20.domain

object FlightManager {
    private val arrivalFlights: MutableList<Arrival> = ArrayList()

    fun getArrivals(): List<Arrival> = arrivalFlights

    fun addArrival(flight: Arrival) {
        arrivalFlights.add(flight)
    }

    init {
        addArrival(Arrival(
            1,
            "Comp",
            "code",
            "Gate A",
            "10-00",
            "11-00",
            "Desk A",
            "Minsk",
            Status.AIRBORNE))
    }
}