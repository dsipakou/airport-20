package com.example.airport20.presentation.flightlist.departure

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.airport20.R
import com.example.airport20.domain.Departure
import kotlinx.android.synthetic.main.fragment_departure.view.*

typealias DepartureClickListener = (Departure) -> Unit

class MyDepartureRecyclerViewAdapter(private val clickListener: DepartureClickListener) :
    RecyclerView.Adapter<MyDepartureRecyclerViewAdapter.ViewHolder>() {

    private var flightList: List<Departure> = emptyList<Departure>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_departure, parent, false)
        val viewHolder = ViewHolder(view)
        view.setOnClickListener {
            clickListener(flightList[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = flightList[position]
        holder.mCityView.text = item.city
        holder.mCodeView.text = item.code
        holder.mCompanyView.text = item.company
        holder.mExpectedTimeView.text = item.expectedTime
        holder.mActualTimeView.text = item.actualTime
        holder.mStatusView.text = item.status.toString()
    }

    fun updateList(flightList: List<Departure>) {
        this.flightList = flightList
    }

    override fun getItemCount(): Int = flightList.size

    inner class ViewHolder(val mView: View): RecyclerView.ViewHolder(mView) {
        val mCityView: TextView = mView.departure_city
        val mCodeView: TextView = mView.departure_code
        val mCompanyView: TextView = mView.departure_company
        val mExpectedTimeView: TextView = mView.departure_expected_time
        val mActualTimeView: TextView = mView.departure_actual_time
        val mStatusView: TextView = mView.departure_status

        override fun toString(): String {
            return super.toString() + " '" + mCityView.text + "'"
        }
    }
}
