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
import com.example.airport20.utils.ParseTimetable
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
            flowState.value = loading()
            ParseTimetable().getArrivals()

            val mArrivals: List<Arrival> = FlightManager.getArrivals()
            val db = FirebaseFirestore.getInstance()
            try {
                for ((index, value) in mArrivals.withIndex()) {
                    if (value.cityCode != "") {
                        val citiesRef = db.collection("cities").document(value.cityCode)
                        citiesRef.get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    try {
                                        currentCity = document.toObject(City::class.java)
                                        if (Locale.getDefault().toString() == "ru") {
                                            mArrivals[index].city = currentCity?.ru?.get("city") ?: value.city
                                        } else {
                                            mArrivals[index].city = currentCity?.en?.get("city") ?: value.city
                                        }
                                        mArrivals[index].imageUrl = currentCity?.imageUrl ?: value.imageUrl
                                        arrivals.postValue(mArrivals)
                                        Log.d("FireBase Arrival List", "DocumentSnapshot data: ${currentCity?.en}")
                                    } catch (e: Exception) {
                                        Log.e("FireBase Arrival List", e.toString())
                                    }
                                } else {
                                    Log.d("FireBase Arrival List", "No such document")
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.d("FireBase Arrival List", "get failed with ", exception)
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