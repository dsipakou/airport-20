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
        holder.mIdView.text = item.id.toString()
        holder.mContentView.text = item.city
    }

    fun updateList(flightList: List<Departure>) {
        this.flightList = flightList
    }

    override fun getItemCount(): Int = flightList.size

    inner class ViewHolder(val mView: View): RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.departure_item_number
        val mContentView: TextView = mView.departure_content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
