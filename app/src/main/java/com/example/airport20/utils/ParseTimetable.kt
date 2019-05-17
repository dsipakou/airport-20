package com.example.airport20.utils

import com.example.airport20.domain.Arrival
import com.example.airport20.domain.Departure
import com.example.airport20.domain.FlightManager
import com.example.airport20.domain.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.util.*

class ParseTimetable {
    suspend fun getArrivals() = withContext(Dispatchers.IO) {
        launch {
            val document = Jsoup.connect(ARRIVAL_URL).get()
            val tr = document.select("div#content-bottom tr.today")
            tr.forEach {
                val tds = it.select("td")
                FlightManager.addArrival(
                    Arrival(
                        UUID.randomUUID().toString(),
                        tds[0].text(),
                        tds[3].text(),
                        tds[5].text(),
                        tds[1].text(),
                        tds[2].text(),
                        "",
                        tds[4].text(),
                        sanitizeString(tds[4].text()),
                        Status.fromString(tds[6].text()),
                        "")
                )
            }
        }

    }

    suspend fun getDepartures() = withContext(Dispatchers.IO) {
        async {
            val document = Jsoup.connect(DEPARTURE_URL).get()
            val tr = document.select("div#content-bottom tr.today")
            tr.forEach {
                val tds = it.select("td")
                FlightManager.addDeparture(
                    Departure(
                        UUID.randomUUID().toString(),
                        tds[0].text(),
                        tds[2].text(),
                        tds[5].text(),
                        tds[1].text(),
                        "",
                        tds[4].text(),
                        tds[3].text(),
                        sanitizeString(tds[3].text()),
                        Status.fromString(tds[6].text()),
                        ""
                    )
                )
            }
        }
    }
}