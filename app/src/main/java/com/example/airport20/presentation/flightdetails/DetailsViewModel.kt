package com.example.airport20.presentation.flightdetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airport20.domain.City
import com.example.airport20.domain.Flight
import com.example.airport20.domain.FlightManager.getFlight
import com.example.airport20.domain.FlightType
import com.example.airport20.utils.sanitizeString
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class DetailsViewModel : ViewModel() {
    private val flight = MutableLiveData<Flight>()

    val observableFlight: LiveData<Flight>
        get() = flight

    fun setFlight(id: String, type: FlightType) {
        var mFlight: Flight = getFlight(id, type)
        val db = FirebaseFirestore.getInstance()
        val citiesRef = db.collection("cities").document(mFlight.cityCode)
        val airlineRef = db.collection("airlines").document(sanitizeString(mFlight.companyCode))
        citiesRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    currentCity = document.toObject(City::class.java)
                    if (Locale.getDefault().toString() == "ru") {
                        mFlight.city = currentCity?.ru?.get("city") ?: mFlight.city
                        mFlight.airport = currentCity?.ru?.get("airport") ?: ""
                    } else {
                        mFlight.city = currentCity?.en?.get("city") ?: mFlight.city
                        mFlight.airport = currentCity?.en?.get("airport") ?: ""
                    }
                    mFlight.imageUrl = currentCity?.imageUrl ?: mFlight.imageUrl
                    flight.postValue(mFlight)
                    Log.d("FireBase", "DocumentSnapshot data: ${currentCity?.en}")
                } else {
                    Log.d("FireBase", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("FireBase", "get failed with ", exception)
            }
        airlineRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    try {
                        val imageUrl = document.get("imageUrl")
                        if (imageUrl != null) {
                            mFlight.companyUrl = imageUrl.toString()
                        }
                        flight.postValue(mFlight)
                    } catch (e: Exception) {
                        Log.e("FireBase Arrival List", "Can't get name for $document")
                    }
                }
            }
        flight.value = mFlight
    }

    companion object {
        var currentCity: City? = null
    }
}