package com.example.airport20.domain

import com.beust.klaxon.Json

data class Departure (
    override val id: String,
//    @Json(path = "$.airline.title")
    override var company: String,
//    @Json(path = "$.airline.title")
    override var companyCode: String,
    override var companyUrl: String,
    override var airport: String,
    @Json(name = "flight")
    override val code: String,
//    @Json(name = "numbers_gate")
    override val gate: String,
//    @Json(name = "plan")
    override val expectedTime: AirportTime,
//    @Json(name = "fact")
    override val actualTime: AirportTime,
//    @Json(name = "numbers_reg")
    override val registrationDesk: String,
//    @Json(path = "$.airport.title")
    override var city: String,
//    @Json(path = "$.airport.title")
    override var cityCode: String,
//    @Json(path = "$.status.id")
    override val status: Status,
    override var imageUrl: String = ""
) : Flight