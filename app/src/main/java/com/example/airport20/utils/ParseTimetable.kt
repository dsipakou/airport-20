package com.example.airport20.utils

import com.example.airport20.domain.Arrival
import com.example.airport20.domain.FlightManager
import com.example.airport20.domain.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.util.*
import kotlin.collections.ArrayList

class ParseTimetable {
    private val arrivals: MutableList<Arrival> = ArrayList()

    suspend fun getArrivals() = withContext(Dispatchers.IO) {
        val document = Jsoup.connect(ARRIVAL_URL).get()
        val tr = document.select("div#content-bottom tr.yesterday")
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
                    Status.valueOf(tds[6].text()),
                    "")
            )
        }
    }
}