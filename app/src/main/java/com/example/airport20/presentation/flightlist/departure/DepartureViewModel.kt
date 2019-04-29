package com.example.airport20.presentation.flightlist.departure

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airport20.domain.City
import com.example.airport20.domain.Departure
import com.example.airport20.domain.FlightManager
import com.example.airport20.utils.sanitizeString
import com.google.firebase.firestore.FirebaseFirestore

class DepartureViewModel: ViewModel() {
    private val departures = MutableLiveData<List<Departure>>()

    val observableDepartureList: LiveData<List<Departure>>
        get() = departures

    init {
        load()
    }

    fun load() {
        var mDepartures: List<Departure> = FlightManager.getDepartures()
        val db = FirebaseFirestore.getInstance()
        for ((index, value) in mDepartures.withIndex()) {
            val city = sanitizeString(value.city).toLowerCase()
            Log.d("FireBase Arrival List", "Current city is: $city")
            val citiesRef = db.collection("cities").document(city)
            citiesRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        currentCity = document.toObject(City::class.java)
                        mDepartures[index].city = currentCity?.en?.get("city") ?: value.city
                        mDepartures[index].imageUrl = currentCity?.imageUrl ?: value.imageUrl
                        departures.postValue(mDepartures)
                        Log.d("FireBase Arrival List", "DocumentSnapshot data: ${currentCity?.en}")
                    } else {
                        Log.d("FireBase Arrival List", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("FireBase Arrival List", "get failed with ", exception)
                }
        }

        departures.value = mDepartures
    }

    companion object {
        var currentCity: City? = null
    }
}