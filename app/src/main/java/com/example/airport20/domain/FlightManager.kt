package com.example.airport20.domain

import kotlin.collections.ArrayList

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

    fun getFlight(id: String, flightType: FlightType): Flight {
        val flight: Flight? = when (flightType) {
            FlightType.ARRIVAL -> arrivalFlights.find { it.id == id }
            FlightType.DEPARTURE -> departureFlights.find { it.id == id }
        }
        return flight!!
    }

    init {}
}