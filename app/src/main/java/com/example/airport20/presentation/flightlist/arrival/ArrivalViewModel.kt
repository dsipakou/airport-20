package com.example.airport20.presentation.flightlist.arrival

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airport20.domain.Arrival
import com.example.airport20.domain.FlightManager


class ArrivalViewModel: ViewModel() {
    private val arrivals = MutableLiveData<List<Arrival>>()

    val observalbeArrivalList: LiveData<List<Arrival>>
        get() = arrivals

    init {
        load()
    }

    fun load() {
        arrivals.value = FlightManager.getArrivals()
    }
}