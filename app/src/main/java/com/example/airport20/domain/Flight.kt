package com.example.airport20.domain

interface Flight {
    val id: Int
    val company: String
    val code: String
    val gate: String
    val expectedTime: String
    val actualTime: String
    val registrationDesk: String
    var city: String
    var cityCode: String
    val status: Status
    var imageUrl: String
}