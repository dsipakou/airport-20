package com.example.airport20.domain

data class Arrival(
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
    override val aircraft: String,
    override var city: String,
    override var cityCode: String,
    override val status: Status,
    override var imageUrl: String = ""
) : Flight