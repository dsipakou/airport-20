package com.example.airport20.domain

import android.content.res.Resources

object FlightManager {
    private val arrivalFlights: MutableList<Arrival> = ArrayList()
    private val departureFlights: MutableList<Departure> = ArrayList()

    fun getArrivals(): List<Arrival> = arrivalFlights

    fun getDepartures(): List<Departure> = departureFlights

    fun addArrival(flight: Arrival) {
        arrivalFlights.add(flight)
    }

    fun addDeparture(flight: Departure) {
        departureFlights.add(flight)
    }

    fun getFlight(id: Int, flightType: FlightType): Flight {
        val flight: Flight? = when (flightType) {
            FlightType.ARRIVAL -> arrivalFlights.find { it.id == id }
            FlightType.DEPARTURE -> departureFlights.find { it.id == id }
        }
        return flight!!
    }

    init {
        addArrival(
            Arrival(
                1,
                "Comp",
                "code",
                "Gate A",
                "10-00",
                "11-00",
                "Desk A",
                "Minsk",
                Status.AIRBORNE
            )
        )
        addArrival(
            Arrival(
                2,
                "Comp2",
                "code2",
                "Gate B",
                "12-00",
                "13-00",
                "Desk B",
                "Frankfurt",
                Status.DELAYED
            )
        )
        addDeparture(
            Departure(
                3,
                "CompDep",
                "codeDep",
                "Gate D",
                "14-15",
                "14-30",
                "Desk D",
                "Berlin",
                Status.ENROUTE
            )
        )
    }
}