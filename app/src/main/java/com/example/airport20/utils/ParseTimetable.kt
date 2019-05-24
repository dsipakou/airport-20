package com.example.airport20.utils

import com.example.airport20.domain.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.util.*

class ParseTimetable {
    suspend fun getArrivals() = withContext(Dispatchers.IO) {
        launch {
            val url = getUrl(FlightType.ARRIVAL)
            val period = getPeriod()
            val document = Jsoup.connect(url).get()
            val tr = document.select("div#content-bottom tr$period")
            tr.forEach {
                val tds = it.select("td")
                if (tds.size > 0) {
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

    }

    suspend fun getDepartures() = withContext(Dispatchers.IO) {
        launch {
            val url = getUrl(FlightType.DEPARTURE)
            val period = getPeriod()
            val document = Jsoup.connect(url).get()
            val tr = document.select("div#content-bottom tr$period")
            tr.forEach {
                val tds = it.select("td")
                if (tds.size > 0) {
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

    private fun getUrl(type: FlightType): String {
        if (FlightManager.getPeriod() == TimeRange.NOW) {
            when(type) {
                FlightType.ARRIVAL -> return SHORT_ARRIVAL_URL
                FlightType.DEPARTURE -> return SHORT_DEPARTURE_URL
            }
        } else {
            when(type) {
                FlightType.ARRIVAL -> return ARRIVAL_URL
                FlightType.DEPARTURE -> return DEPARTURE_URL
            }
        }
    }

    private fun getPeriod(): String {
        if (FlightManager.getPeriod() == TimeRange.NOW) {
            return ""
        }
        return ".${FlightManager.getPeriod().time}"
    }
}