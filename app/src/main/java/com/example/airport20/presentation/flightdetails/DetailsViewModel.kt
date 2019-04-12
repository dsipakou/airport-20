package com.example.airport20.presentation.flightdetails


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airport20.dummy.DummyContent

class DetailsViewModel : ViewModel() {
    private val flight = MutableLiveData<DummyContent.DummyItem>()

    val observableFlight: LiveData<DummyContent.DummyItem>
        get() = flight

    fun getFlight(id: Int) {
        flight.value = DummyContent.ITEMS[0]
    }
}