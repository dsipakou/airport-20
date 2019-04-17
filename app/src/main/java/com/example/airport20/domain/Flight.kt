package com.example.airport20.domain

interface Flight {
    val id: Int
    val company: String
    val code: String
    val gate: String
    val expectedTime: String
    val actualTime: String
    val registrationDesk: String
    val city: String
    val status: Status
}