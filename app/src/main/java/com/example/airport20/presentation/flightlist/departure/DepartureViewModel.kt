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
            flowState.value = FlowState.loading()
            ParseTimetable().getDepartures()

            var mDepartures: List<Departure> = FlightManager.getDepartures()
            val db = FirebaseFirestore.getInstance()
            try {
                for ((index, value) in mDepartures.withIndex()) {
                    if (value.cityCode != "") {
                        val citiesRef = db.collection("cities").document(value.cityCode)
                        citiesRef.get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    try {
                                        currentCity = document.toObject(City::class.java)
                                        if (Locale.getDefault().toString() == "ru") {
                                            mDepartures[index].city = currentCity?.ru?.get("city") ?: value.city
                                        } else {
                                            mDepartures[index].city = currentCity?.en?.get("city") ?: value.city
                                        }
                                        mDepartures[index].imageUrl = currentCity?.imageUrl ?: value.imageUrl
                                        departures.postValue(mDepartures)
                                        Log.d("FireBase Departure List", "DocumentSnapshot data: ${currentCity?.en}")
                                    } catch (e: Exception) {
                                        Log.e("FireBase Departure List", e.toString())
                                    }
                                } else {
                                    Log.d("FireBase Departure List", "No such document")
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.d("FireBase Departure List", "get failed with ", exception)
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
