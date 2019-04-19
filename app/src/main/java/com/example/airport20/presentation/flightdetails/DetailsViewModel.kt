package com.example.airport20.presentation.flightdetails


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airport20.domain.Flight
import com.example.airport20.domain.FlightManager.getFlight
import com.example.airport20.domain.FlightType

class DetailsViewModel : ViewModel() {
    private val flight = MutableLiveData<Flight>()

    val observableFlight: LiveData<Flight>
        get() = flight

    fun setFlight(id: Int, type: FlightType) {
        flight.value = getFlight(id, type)
    }
}