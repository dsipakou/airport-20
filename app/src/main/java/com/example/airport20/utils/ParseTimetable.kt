package com.example.airport20.utils

import android.util.Log
import com.example.airport20.domain.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class ParseTimetable {
    suspend fun getArrivals() = withContext(Dispatchers.IO) {
        launch {
            try {
                fun setFlights(entity: JSONObject) {
                    try {
//                    val status: Status
                        val statusObject = JSONObject(entity.getString("status"))
                        val status = statusObject.getString("id")
                        val regex = Regex("([A-Z- ]+)\\s+(expected|departure)[a-z ]+(\\d+:\\d+)")
                        val actualTime: AirportTime = Dates.getAirportTime(entity.getString("plan"))
//                    val res = regex.find(arrival.text())
//                    if (res != null) {
//                        actualTime = parseTime(res.groups[3]!!.value)
//                        status = Status.fromString(res.groups[1]!!.value)
//                    } else {
//                        actualTime = parseTime(arrival[2].text())
//                        status = Status.fromString(arrival[6].text())
//                    }
                        val id = UUID.randomUUID().toString()
//                    val id = JSONObject(entity.getString("flight_id"))
                        val companyObject = JSONObject(entity.getString("airline"))
                        val company = companyObject.getString("title")
                        val code = entity.getString("flight")
                        val gate = entity.getString("gate")
                        val cityObject = JSONObject(entity.getString("airport"))
                        val city = cityObject.getString("title")
                        var expectedTime: AirportTime = AirportTime(null, null)
                        if (entity.getString("fact") != "null") {
                            expectedTime = Dates.getAirportTime(entity.getString("fact"))
                        }
//                    val expectedTime = parseTime(arrival[1].text())
                        val cityCode = sanitizeString(city)
                        if (cityCode.isNotEmpty()) {
                            FlightManager.addArrival(
                                Arrival(
                                    id = id,
                                    company = company,
                                    companyCode = company,
                                    companyUrl = "",
                                    airport = "",
                                    code = code,
                                    gate = gate,
                                    expectedTime = expectedTime,
                                    actualTime = actualTime,
                                    registrationDesk = "",
                                    city = city,
                                    cityCode = cityCode,
                                    status = Status.fromString(status),
                                    imageUrl = ""
                                )
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("Parse flight", "Can't parse flight: " + e.stackTrace)
                    }
                }
                val url = getUrl(FlightType.ARRIVAL)
                val period = getPeriod()
                val okHttpClient = OkHttpClient()
                val parsedResponse = parseResponse(okHttpClient.newCall(createRequest(url)).execute())
                val arrivals = JSONArray(parsedResponse)
//                val document = Jsoup.connect(url).get()
//                val tr = document.select("table#fl-arrival tbody tr$period")
                for (i in 0 until arrivals.length()) {
                    if (i > 85) {
                        Log.i("Hello", "there")
                    }
                    val entity = JSONObject(arrivals.get(i).toString())
                    val flightTime: Date = Dates.parseTime(entity.getString("plan"))
                    when (FlightManager.getPeriod()) {
                        TimeRange.NOW -> {
                            if (Date().addHours(-1) < flightTime && flightTime < Date().addHours(2)) {
                                setFlights(entity)
                            }
                        }
                        TimeRange.YESTERDAY -> {
                            if (flightTime.isYesterday()) {
                                setFlights(entity)
                            }
                        }
                        TimeRange.TODAY -> {
                            if (flightTime.isToday()) {
                                setFlights(entity)
                            }
                        }
                        TimeRange.TOMORROW -> {
                            if (flightTime.isTomorrow()) {
                                setFlights(entity)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("Parse timetable", "Can't get data from airport.by")
            }
        }

    }

    suspend fun getDepartures() = withContext(Dispatchers.IO) {
        launch {
            val url = getUrl(FlightType.DEPARTURE)
            val period = getPeriod()
            try {
                val document = Jsoup.connect(url).get()
                val tr = document.select("div#content-bottom tr$period")
                tr.forEach {
                    val tds = it.select("td")
                    if (tds.size > 0) {
                        val actualTime: AirportTime
                        val status: Status
                        val regex = Regex("([A-Z- ]+)\\s+(expected|departure)[a-z ]+(\\d+:\\d+)")
                        val res = regex.find(tds[6].text())
                        if (res != null) {
                            actualTime = Dates.getAirportTime(res.groups[3]!!.value)
                            status = Status.fromString(res.groups[1]!!.value)
                        } else {
                            actualTime = AirportTime(null, null)
                            status = Status.fromString(tds[6].text())
                        }
                        val id = UUID.randomUUID().toString()
                        val company = tds[0].text()
                        val code = tds[2].text()
                        val gate = tds[5].text()
                        val expectedTime = Dates.getAirportTime(tds[1].text())
                        val registrationDesk = tds[4].text()
                        val city = tds[3].text()
                        val cityCode = sanitizeString(tds[3].text())
                        if (cityCode.isNotEmpty()) {
                            FlightManager.addDeparture(
                                Departure(
                                    id = id,
                                    company = company,
                                    companyCode = company,
                                    companyUrl = "",
                                    airport = "",
                                    code = code,
                                    gate = gate,
                                    expectedTime = expectedTime,
                                    actualTime = actualTime,
                                    registrationDesk = registrationDesk,
                                    city = city,
                                    cityCode = cityCode,
                                    status = status,
                                    imageUrl = ""
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("Parse timetable", "Can't get data from airport.by")
            }
        }
    }

    private fun createRequest(url: String): Request {
        return Request.Builder()
            .url(url)
            .build()
    }

    private fun parseResponse(response: Response): String {
        val body = response.body?.string() ?: ""
        return body
    }

    private fun getUrl(type: FlightType): String {
        if (FlightManager.getPeriod() == TimeRange.NOW) {
            when (type) {
                FlightType.ARRIVAL -> return ARRIVAL_URL
                FlightType.DEPARTURE -> return DEPARTURE_URL
            }
        } else {
            when (type) {
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