package com.example.airport20.presentation.flightlist.departure

import android.util.Log
import androidx.lifecycle.*
import com.example.airport20.domain.City
import com.example.airport20.domain.Departure
import com.example.airport20.domain.FlightManager
import com.example.airport20.utils.ParseTimetable
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import androidx.lifecycle.viewModelScope
import com.example.airport20.utils.FlowState
import com.example.airport20.utils.FlowStatus
import com.example.airport20.utils.sanitizeString
import java.util.*

class DepartureViewModel : ViewModel(), LifecycleObserver {
    private val departures = MutableLiveData<List<Departure>>()
    private val flowState = MutableLiveData<FlowState<MutableList<Departure>>>()
    var loading = false

    val observableDepartureList: LiveData<List<Departure>>
        get() = departures

    init {
        load()
    }

    fun load() {
        if (loading) return
        departures.value = emptyList()
        loading = true
        flowState.value = FlowState.loading()
        viewModelScope.launch {
            ParseTimetable().getDepartures()

            var mDepartures: List<Departure> = FlightManager.getDepartures()
            val db = FirebaseFirestore.getInstance()
            try {
                for ((index, value) in mDepartures.withIndex()) {
                    val city = value.city
                    mDepartures[index].city = ""
                    if (value.cityCode != "") {
                        val citiesRef = db.collection("cities").document(value.cityCode)
                        val airlineRef = db.collection("airlines").document(sanitizeString(value.companyCode))
                        citiesRef.get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    try {
                                        if (mDepartures.isNotEmpty() && mDepartures.size > index) {
                                            currentCity = document.toObject(City::class.java)
                                            if (Locale.getDefault().toString() == "ru") {
                                                mDepartures[index].city = currentCity?.ru?.get("city") ?: city
                                            } else {
                                                mDepartures[index].city = currentCity?.en?.get("city") ?: city
                                            }
                                            mDepartures[index].imageUrl = currentCity?.imageUrl ?: value.imageUrl
                                            departures.postValue(mDepartures)
                                            Log.d(
                                                "FireBase Departure List",
                                                "DocumentSnapshot data: ${currentCity?.en}"
                                            )
                                        }
                                    } catch (e: Exception) {
                                        if (mDepartures.isNotEmpty() && mDepartures.size > index) {
                                            mDepartures[index].city = city
                                            departures.postValue(mDepartures)
                                            Log.e("FireBase Departure List", e.toString())
                                        }
                                    }
                                } else {
                                    if (mDepartures.isNotEmpty() && mDepartures.size > index) {
                                        mDepartures[index].city = city
                                        Log.d("FireBase Departure List", "No such document")
                                    }
                                }
                            }
                            .addOnFailureListener { exception ->
                                if (mDepartures.isNotEmpty() && mDepartures.size > index) {
                                    mDepartures[index].city = city
                                    Log.d("FireBase Departure List", "get failed with ", exception)
                                }
                            }
                        airlineRef.get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    try {
                                        if (mDepartures.isNotEmpty() && mDepartures.size > index) {
                                            val name = document.get("name")
                                            if (name != null) {
                                                mDepartures[index].company = name.toString()
                                            }
                                            departures.postValue(mDepartures)
                                        }
                                    } catch (e: Exception) {
                                        departures.postValue(mDepartures)
                                        Log.e("FireBase Arrival List", "Can't get name for $document")
                                    }
                                }
                            }
                    }
                }
            } catch (e: Exception) {
                Log.e("FireBase Departure List", e.toString())
            }

            departures.value = mDepartures
            flowState.value = FlowState.success()
            loading = false
        }
    }

    fun refresh() {
        if (loading) viewModelScope.coroutineContext.cancelChildren()
        loading = false
        load()
    }

    fun getMainFlow(): LiveData<FlowState<MutableList<Departure>>> = flowState

    companion object {
        var currentCity: City? = null
    }
}
