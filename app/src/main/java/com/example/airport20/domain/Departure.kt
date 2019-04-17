package com.example.airport20.domain

data class Departure (
    override val id: Int,
    override val company: String,
    override val code: String,
    override val gate: String,
    override val expectedTime: String,
    override val actualTime: String,
    override val registrationDesk: String,
    override val city: String,
    override val status: Status
) : Flight