package com.example.airport20.utils

import android.util.Log
import com.beust.klaxon.*
import com.example.airport20.domain.*
import com.example.airport20.domain.Status
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.util.*


@Target(AnnotationTarget.FIELD)
annotation class JsonTime

@Target(AnnotationTarget.FIELD)
annotation class JsonStatus

val timeConverter = object: Converter {
    override fun canConvert(cls: Class<*>) = cls == AirportTime::class.java

    override fun fromJson(jv: JsonValue) =
        if (jv.string != null && jv.string != "null") {
            Dates.getAirportTime(jv.string!!)
        } else {
            Log.w("Parse time", "Time is null")
            null
        }

    override fun toJson(value: Any) = TODO("not implemented")
}

val statusConverter = object: Converter {
    override fun canConvert(cls: Class<*>) = cls == Status::class.java

    override fun fromJson(jv: JsonValue) =
        if (jv?.obj?.get("id") != null) {
            Status.fromString((jv?.obj?.get("id")!! as String))
        } else {
            Log.e("Parse status", "Can't parse status")
            null
        }

    override fun toJson(value: Any) = TODO("not implemented")
}

class ParseTimetable {
    suspend fun getArrivals() = withContext(Dispatchers.IO) {
        launch {
            try {
                fun setFlight(entity: JSONObject) {
                    try {
                        val statusObject = JSONObject(entity.getString("status"))
                        val status = statusObject.getString("id")
                        val expectedTime: AirportTime = Dates.getAirportTime(entity.getString("plan"))
                        val id = UUID.randomUUID().toString()
                        val companyObject = JSONObject(entity.getString("airline"))
                        val company = companyObject.getString("title")
                        val code = entity.getString("flight")
                        val gate = entity.getString("gate")
                        val cityObject = JSONObject(entity.getString("airport"))
                        val city = cityObject.getString("title")
                        val aircraftObject = JSONObject(entity.getString("aircraft"))
                        val aircraft = aircraftObject.getString("title")
                        var actualTime = AirportTime(null, null)
                        if (entity.getString("fact") != "null") {
                            actualTime = Dates.getAirportTime(entity.getString("fact"))
                        }
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
                                    aircraft = aircraft,
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
                val okHttpClient = OkHttpClient()
                val parsedResponse = parseResponse(okHttpClient.newCall(createRequest(url)).execute())
                val arrivals = JSONArray(parsedResponse)
                for (i in 0 until arrivals.length()) {
                    val entity = JSONObject(arrivals.get(i).toString())
                    val flightTime: Date = Dates.parseTime(entity.getString("plan"))
                    when (FlightManager.getPeriod()) {
                        TimeRange.NOW -> {
                            if (Date().addHours(-1) < flightTime && flightTime < Date().addHours(2)) {
                                setFlight(entity)
                            }
                        }
                        TimeRange.YESTERDAY -> {
                            if (flightTime.isYesterday()) {
                                setFlight(entity)
                            }
                        }
                        TimeRange.TODAY -> {
                            if (flightTime.isToday()) {
                                setFlight(entity)
                            }
                        }
                        TimeRange.TOMORROW -> {
                            if (flightTime.isTomorrow()) {
                                setFlight(entity)
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
            try {
                fun setFlight(entity: JSONObject) {
                    try {
                        val id = UUID.randomUUID().toString()
                        val statusObject = JSONObject(entity.getString("status"))
                        val status = statusObject.getString("id")
                        val expectedTime: AirportTime = Dates.getAirportTime(entity.getString("plan"))
                        val companyObject = JSONObject(entity.getString("airline"))
                        val company = companyObject.getString("title")
                        val code = entity.getString("flight")
                        val gateObject = entity.getJSONArray("numbers_gate")
                        val gate = gateObject.join(", ").replace("\"", "")
                        val cityObject = JSONObject(entity.getString("airport"))
                        val city = cityObject.getString("title")
                        val aircraftObject = JSONObject(entity.getString("aircraft"))
                        val aircraft = aircraftObject.getString("title")
                        var actualTime = AirportTime(null, null)
                        if (entity.getString("fact") != "null") {
                            actualTime = Dates.getAirportTime(entity.getString("fact"))
                        }
                        val registrationObject = entity.getJSONArray("numbers_reg")
                        val registrationDesk = registrationObject.join(", ").replace("\"", "")
                        val cityCode = sanitizeString(city)
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
                                    aircraft = aircraft,
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

                val url = getUrl(FlightType.DEPARTURE)
                val okHttpClient = OkHttpClient()
                val parsedResponse = parseResponse(okHttpClient.newCall(createRequest(url)).execute())
                val departures = JSONArray(parsedResponse)
                for (i in 0 until departures.length()) {
                    val entity = JSONObject(departures.get(i).toString())
                    val flightTime: Date = Dates.parseTime(entity.getString("plan"))
                    when (FlightManager.getPeriod()) {
                        TimeRange.NOW -> {
                            if (Date().addHours(-1) < flightTime && flightTime < Date().addHours(2)) {
                                setFlight(entity)
                            }
                        }
                        TimeRange.YESTERDAY -> {
                            if (flightTime.isYesterday()) {
                                setFlight(entity)
                            }
                        }
                        TimeRange.TODAY -> {
                            if (flightTime.isToday()) {
                                setFlight(entity)
                            }
                        }
                        TimeRange.TOMORROW -> {
                            if (flightTime.isTomorrow()) {
                                setFlight(entity)
                            }
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