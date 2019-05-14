package com.example.airport20.domain

data class Departure (
    override val id: String,
    override val company: String,
    override val code: String,
    override val gate: String,
    override val expectedTime: String,
    override val actualTime: String,
    override val registrationDesk: String,
    override var city: String,
    override var cityCode: String,
    override val status: Status,
    override var imageUrl: String = ""
) : Flight