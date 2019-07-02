package com.example.airport20.domain

data class Departure (
    override val id: String,
    override var company: String,
    override var companyCode: String,
    override var companyUrl: String,
    override var airport: String,
    override val code: String,
    override val gate: String,
    override val expectedTime: AirportTime,
    override val actualTime: AirportTime,
    override val registrationDesk: String,
    override var city: String,
    override var cityCode: String,
    override val status: Status,
    override var imageUrl: String = ""
) : Flight