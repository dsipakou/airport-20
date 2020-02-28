package com.example.airport20.utils

import com.example.airport20.domain.AirportTime
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


object Dates {
    val MINSK_TIME_ZONE = TimeZone.getTimeZone("GMT+3")

    fun from(day: Date, time: Date, timeZone: TimeZone? = null): Date {
        val dateComponents = Calendar.getInstance(timeZone ?: TimeZone.getDefault())
        dateComponents.timeInMillis = day.time

        val timeComponents = Calendar.getInstance(timeZone ?: TimeZone.getDefault())
        timeComponents.timeInMillis = time.time

        dateComponents.set(Calendar.HOUR_OF_DAY, timeComponents.get(Calendar.HOUR_OF_DAY))
        dateComponents.set(Calendar.MINUTE, timeComponents.get(Calendar.MINUTE))
        dateComponents.set(Calendar.SECOND, timeComponents.get(Calendar.SECOND))
        dateComponents.set(Calendar.MILLISECOND, timeComponents.get(Calendar.MILLISECOND))

        return dateComponents.time
    }

    fun parseTime(timeStr: String): Date {
//    val datetime = OffsetDateTime.parse(timeStr)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
//        val date = SimpleDateFormat("dd-MM")
//        val time = SimpleDateFormat("HH:mm")
        return dateFormat.parse(timeStr)
//        val now = dateFormat.format(Date())
//        val cal = Calendar.getInstance(MINSK_TIME_ZONE)
//
//        val re = Regex("(?:(\\d+.\\d+) )?(\\d+:\\d+)\$")
//        val formatter = re.find(timeStr)
//        val airportTime: AirportTime
//        airportTime = AirportTime(date.format(formatedDateTime), time.format(formatedDateTime))
//        return airportTime
    }

    fun getAirportTime(timeStr: String): AirportTime {
        val datetime = parseTime(timeStr)
        val date = SimpleDateFormat("dd-MM")
        val time = SimpleDateFormat("HH:mm")
        return AirportTime(date.format(datetime), time.format(datetime))
    }

    fun getCurrentDate(): String {
        val date = SimpleDateFormat("dd-MM")
        return date.format(Date())
    }

    fun today(timeZone: TimeZone? = null): Date =
        Date().midnight(timeZone = timeZone)


    fun tomorrow(timeZone: TimeZone? = null): Date =
        today(timeZone = timeZone).addDays(1)


    fun yesterday(timeZone: TimeZone? = null): Date =
        today(timeZone = timeZone).addDays(-1)

}

fun Date.midnight(timeZone: TimeZone? = null): Date {
    val calendar = Calendar.getInstance(timeZone ?: TimeZone.getDefault())
    calendar.timeInMillis = this.time
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.time
}

fun Date.addDays(days: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this.time
    calendar.add(Calendar.DAY_OF_YEAR, days)
    return calendar.time
}

fun Date.addHours(hours: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this.time
    calendar.add(Calendar.HOUR_OF_DAY, hours)
    return calendar.time
}

fun Date.addMinutes(minutes: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this.time
    calendar.add(Calendar.MINUTE, minutes)
    return calendar.time
}

fun Date.isToday(timeZone: TimeZone? = null): Boolean =
    isSameDay(Dates.today(timeZone = timeZone), timeZone = timeZone)

fun Date.isTomorrow(timeZone: TimeZone? = null): Boolean =
    isSameDay(Dates.tomorrow(timeZone = timeZone), timeZone = timeZone)

fun Date.isYesterday(timeZone: TimeZone? = null): Boolean =
    isSameDay(Dates.yesterday(timeZone = timeZone), timeZone = timeZone)

fun Date.isSameDay(day: Date, timeZone: TimeZone? = null): Boolean {
    val currentDate = Calendar.getInstance(timeZone ?: TimeZone.getDefault())
    currentDate.timeInMillis = this.time

    val anotherDay = Calendar.getInstance(timeZone ?: TimeZone.getDefault())
    anotherDay.timeInMillis = day.time

    val sameDay = currentDate.get(Calendar.DAY_OF_YEAR) == anotherDay.get(Calendar.DAY_OF_YEAR)
    val sameYear = currentDate.get(Calendar.YEAR) == anotherDay.get(Calendar.YEAR)

    return sameDay && sameYear
}