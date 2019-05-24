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
                    val actualTime: String
                    val status: Status
                    val regex = Regex("([A-Z- ]+)\\s+(expected|departure)[a-z ]+(\\d+:\\d+)")
                    val res = regex.find(tds[6].text())
                    if (res != null) {
                        actualTime = res.groups[3]!!.value
                        status = Status.fromString(res.groups[1]!!.value)
                    }
                    else {
                        actualTime = tds[2].text()
                        status = Status.fromString(tds[6].text())
                    }
                    val id = UUID.randomUUID().toString()
                    val company = tds[0].text()
                    val code = tds[3].text()
                    val gate = tds[5].text()
                    val expectedTime = tds[1].text()
                    val city = tds[4].text()
                    val cityCode = sanitizeString(tds[4].text())
                    FlightManager.addArrival(
                        Arrival(
                            id,
                            company,
                            code,
                            gate,
                            expectedTime,
                            actualTime,
                            "",
                            city,
                            cityCode,
                            status,
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
                    val actualTime: String
                    val status: Status
                    val regex = Regex("([A-Z- ]+)\\s+(expected|departure)[a-z ]+(\\d+:\\d+)")
                    val res = regex.find(tds[6].text())
                    if (res != null) {
                        actualTime = res.groups[3]!!.value
                        status = Status.fromString(res.groups[1]!!.value)
                    }
                    else {
                        actualTime = ""
                        status = Status.fromString(tds[6].text())
                    }
                    val id = UUID.randomUUID().toString()
                    val company = tds[0].text()
                    val code = tds[2].text()
                    val gate = tds[5].text()
                    val expectedTime = tds[1].text()
                    val registrationDesk = tds[4].text()
                    val city = tds[3].text()
                    val cityCode = sanitizeString(tds[3].text())
                    FlightManager.addDeparture(
                        Departure(
                            id,
                            company,
                            code,
                            gate,
                            expectedTime,
                            actualTime,
                            registrationDesk,
                            city,
                            cityCode,
                            status,
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