package com.example.airport20.presentation.flightlist.arrival

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airport20.domain.Arrival
import com.example.airport20.domain.City
import com.example.airport20.domain.FlightManager
import com.example.airport20.utils.ParseTimetable
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import java.util.*


class ArrivalViewModel: ViewModel() {
    private val arrivals = MutableLiveData<List<Arrival>>()

    val observableArrivalList: LiveData<List<Arrival>>
        get() = arrivals

    init {
        load()
    }

    fun load() {
        runBlocking {
            ParseTimetable().getArrivals()
        }
        var mArrivals: List<Arrival> = FlightManager.getArrivals()
        val db = FirebaseFirestore.getInstance()
        for ((index, value) in mArrivals.withIndex()) {
            if (value.cityCode != "") {
                val citiesRef = db.collection("cities").document(value.cityCode)
                citiesRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            currentCity = document.toObject(City::class.java)
                            if (Locale.getDefault().toString() == "ru") {
                                mArrivals[index].city = currentCity?.ru?.get("city") ?: value.city
                            } else {
                                mArrivals[index].city = currentCity?.en?.get("city") ?: value.city
                            }
                            mArrivals[index].imageUrl = currentCity?.imageUrl ?: value.imageUrl
                            arrivals.postValue(mArrivals)
                            Log.d("FireBase Arrival List", "DocumentSnapshot data: ${currentCity?.en}")
                        } else {
                            Log.d("FireBase Arrival List", "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("FireBase Arrival List", "get failed with ", exception)
                    }
            }
        }

        arrivals.value = mArrivals
    }

    companion object {
        var currentCity: City? = null
    }
}