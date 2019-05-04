package com.example.airport20.domain

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
                "minsk",
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
                "frankfurt",
                Status.DELAYED
            )
        )
        addDeparture(
            Departure(
                3,
                "BELAVIA",
                "B2891",
                "B6",
                "12:15",
                "14-30",
                "25 26 27 28 29 30 31 32 33 34",
                "BERLIN (SCHOENEFELD)",
                "berlinschoenefeld",
                Status.AIRBORNE
            )
        )
        addDeparture(
            Departure(
                4,
                "BELAVIA",
                "B2861",
                "B4",
                "12:20",
                "14-37",
                "25 26 27 28 29 30 31 32 33 34",
                "PRAGUE (VACLAV HAVEL)",
                "praguevaclavhavel",
                Status.AIRBORNE
            )
        )
        addDeparture(
            Departure(
                5,
                "TURKISH AIRLINES",
                "TK284",
                "B5",
                "16:25",
                "14-30",
                "35 36 37 38",
                "ISTANBUL NEW AIRPORT",
                "instanbulnewairport",
                Status.AIRBORNE
            )
        )
        addDeparture(
            Departure(
                6,
                "UTAIR",
                "UT836",
                "F12",
                "12:30",
                "14-30",
                "25 26 27 28 29 30 31 32 33 34",
                "MOSCOW (VNUKOVO)",
                "moscowvnukovo",
                Status.CHECKIN
            )
        )
        addDeparture(
            Departure(
                7,
                "BELAVIA",
                "B2829",
                "B3",
                "17:20",
                "14-30",
                "25 26 27 28 29 30 31 32 33 34",
                "KIEV (ZHULYANY)",
                "kievzhulyany",
                Status.BOARDING_FINISHED
            )
        )
        addDeparture(
            Departure(
                8,
                "LUFTHANSA GERMAN AIRLINES",
                "LH1487",
                "B9",
                "14:00",
                "14-30",
                "3 4",
                "FRANKFURT",
                "frankfurt",
                Status.ENROUTE
            )
        )
        addDeparture(
            Departure(
                9,
                "Lufthansa",
                "MSQ123",
                "Gate R",
                "14-15",
                "14-30",
                "Desk R",
                "ABU-DHABI",
                "abudhabi",
                Status.ENROUTE
            )
        )
        addDeparture(
            Departure(
                3,
                "Lufthansa",
                "MSQ123",
                "Gate R",
                "14-15",
                "14-30",
                "Desk R",
                "Minsk",
                "minsk",
                Status.ENROUTE
            )
        )
        addDeparture(
            Departure(
                3,
                "Lufthansa",
                "MSQ123",
                "Gate R",
                "14-15",
                "14-30",
                "Desk R",
                "Minsk",
                "minsk",
                Status.ENROUTE
            )
        )
        addDeparture(
            Departure(
                3,
                "Lufthansa",
                "MSQ123",
                "Gate R",
                "14-15",
                "14-30",
                "Desk R",
                "Minsk",
                "minsk",
                Status.ENROUTE
            )
        )
    }
}