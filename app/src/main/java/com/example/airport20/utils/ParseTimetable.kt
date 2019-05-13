package com.example.airport20.utils

import android.util.Log
import com.example.airport20.domain.Arrival
import org.jsoup.Jsoup

class ParseTimetable {
    private val arrivals: MutableList<Arrival> = ArrayList()

    fun getArrivals(): List<Arrival> {
        try {
            val document = Jsoup.connect("https://google.com").get()
//            val tr = document.select("div#content-bottom tr.yesterday")
        } catch (e: Exception) {
            Log.e("Exception details", e.message)
        }


//        val doc = Jsoup.connect(SHORT_ARRIVAL_URL).get().run {
//            select("div#content-bottom tr.yesterday").forEachIndexed { index, element ->
//                Log.i("Parser", element.html())
//            }
//        }
        return arrivals
    }
}