package com.example.airport20.presentation.flightlist.arrival

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.airport20.R
import com.example.airport20.domain.Arrival


import kotlinx.android.synthetic.main.fragment_arrival.view.*

typealias ArrivalClickListener = (Arrival) -> Unit

class MyArrivalRecyclerViewAdapter(
    private val clickListener: ArrivalClickListener
) : RecyclerView.Adapter<MyArrivalRecyclerViewAdapter.ViewHolder>() {

    private var flightList: List<Arrival> = emptyList<Arrival>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_arrival, parent, false)
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

    fun updateList(flightList: List<Arrival>) {
        this.flightList = flightList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = flightList.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mCityView: TextView = mView.arrival_city
        val mCodeView: TextView = mView.arrival_code
        val mCompanyView: TextView = mView.arrival_company
        val mExpectedTimeView: TextView = mView.arrival_expected_time
        val mActualTimeView: TextView = mView.arrival_actual_time
        val mStatusView: TextView = mView.arrival_status

        override fun toString(): String {
            return super.toString() + " '" + mCityView.text + "'"
        }
    }
}
