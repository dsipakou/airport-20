package com.example.airport20.presentation.flightlist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.airport20.R
import com.example.airport20.dummy.DummyContent


import com.example.airport20.presentation.flightlist.arrival.ArrivalFragment.OnListFragmentInteractionListener
import com.example.airport20.dummy.DummyContent.DummyItem

import kotlinx.android.synthetic.main.fragment_arrival.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */

typealias ClickListener = (DummyItem) -> Unit

class MyArrivalRecyclerViewAdapter(
    private val clickListener: ClickListener
) : RecyclerView.Adapter<MyArrivalRecyclerViewAdapter.ViewHolder>() {

    private var flightList = DummyContent.ITEMS

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
        holder.mIdView.text = item.id
        holder.mContentView.text = item.content
    }

    override fun getItemCount(): Int = flightList.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
