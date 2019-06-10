package com.example.airport20.presentation.flightlist.arrival

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airport20.domain.Arrival
import com.example.airport20.domain.City
import com.example.airport20.domain.FlightManager
import com.example.airport20.utils.FlowState
import com.example.airport20.utils.FlowState.Companion.loading
import com.example.airport20.utils.FlowState.Companion.success
import com.example.airport20.utils.FlowStatus
import com.example.airport20.utils.ParseTimetable
import com.example.airport20.utils.sanitizeString
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import java.util.*


class ArrivalViewModel : ViewModel() {
    private val arrivals = MutableLiveData<List<Arrival>>()
    private val flowState = MutableLiveData<FlowState<MutableList<Arrival>>>()
    var loading = false

    val observableArrivalList: LiveData<List<Arrival>>
        get() = arrivals

    init {
        load()
    }

    fun load() {
        if (loading) return
        loading = true
        flowState.value = loading()
        viewModelScope.launch {
            ParseTimetable().getArrivals()
            flowState.value = loading()
            val mArrivals: List<Arrival> = FlightManager.getArrivals()
            val db = FirebaseFirestore.getInstance()
            try {
                for ((index, value) in mArrivals.withIndex()) {
                    val city = mArrivals[index].city
                    mArrivals[index].city = ""
                    if (value.cityCode != "") {
                        val citiesRef = db.collection("cities").document(value.cityCode)
                        val airlineRef = db.collection("airlines").document(sanitizeString(value.company))
                        citiesRef.get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    try {
                                        if (mArrivals.isNotEmpty() && mArrivals.size > index) {
                                            currentCity = document.toObject(City::class.java)
                                            if (Locale.getDefault().toString() == "ru") {
                                                mArrivals[index].city = currentCity?.ru?.get("city") ?: city
                                            } else {
                                                mArrivals[index].city = currentCity?.en?.get("city") ?: city
                                            }
                                            mArrivals[index].imageUrl = currentCity?.imageUrl ?: value.imageUrl
                                            arrivals.postValue(mArrivals)
                                            Log.d("FireBase Arrival List", "DocumentSnapshot data: ${currentCity?.en}")
                                        }
                                    } catch (e: Exception) {
                                        if (mArrivals.isNotEmpty() && mArrivals.size > index) {
                                            mArrivals[index].city = city
                                            arrivals.postValue(mArrivals)
                                            Log.e("FireBase Arrival List", e.toString())
                                        }
                                    }
                                } else {
                                    if (mArrivals.isNotEmpty() && mArrivals.size > index) {
                                        mArrivals[index].city = city
                                        arrivals.postValue(mArrivals)
                                        Log.d("FireBase Arrival List", "No such document")
                                    }
                                }
                            }
                            .addOnFailureListener { exception ->
                                if (mArrivals.isNotEmpty() && mArrivals.size > index) {
                                    mArrivals[index].city = city
                                    arrivals.postValue(mArrivals)
                                    Log.d("FireBase Arrival List", "get failed with ", exception)
                                }
                            }
                        airlineRef.get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    try {
                                        if (mArrivals.isNotEmpty() && mArrivals.size > index) {
                                            val name = document.get("name")
                                            if (name != null) {
                                                mArrivals[index].company = name.toString()
                                            }
                                        }
                                    } catch (e: Exception) {
                                        Log.e("FireBase Arrival List", "Can't get name for $document")
                                    }
                                }
                            }
                    }
                }
            } catch (e: Exception) {
                Log.e("FireBase Arrival List", e.toString())
            }
            arrivals.value = mArrivals
            flowState.value = success()
            loading = false
        }
    }

    fun refresh() {
        if (loading) viewModelScope.coroutineContext.cancelChildren()
        loading = false
        load()
    }

    fun getMainFlow(): LiveData<FlowState<MutableList<Arrival>>> = flowState

    companion object {
        var currentCity: City? = null
    }
}