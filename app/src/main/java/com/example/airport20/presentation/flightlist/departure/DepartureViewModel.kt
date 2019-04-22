package com.example.airport20.presentation.flightlist.departure

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airport20.domain.Departure
import com.example.airport20.domain.FlightManager

class DepartureViewModel: ViewModel() {
    private val departures = MutableLiveData<List<Departure>>()

    val observableDepartureList: LiveData<List<Departure>>
        get() = departures

    init {
        load()
    }

    fun load() {
        departures.value = FlightManager.getDepartures()
    }
}