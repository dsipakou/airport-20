package com.example.airport20.presentation.flightlist.departure


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController

import com.example.airport20.R
import com.example.airport20.domain.Departure
import com.example.airport20.domain.FlightType
import com.example.airport20.presentation.flighttabs.TabsFragmentDirections.actionTabsFragment2ToDetailsFragment
import kotlinx.android.synthetic.main.fragment_departure_list.*
import androidx.recyclerview.widget.DividerItemDecoration
import android.graphics.drawable.ClipDrawable.HORIZONTAL
import com.example.airport20.MainActivity
import com.example.airport20.utils.FlowState
import com.example.airport20.utils.FlowStatus


class DepartureFragment : Fragment(), MainActivity.OnHeadlineSelectedListener {

    private val clickListener: DepartureClickListener = this::onFlightClicked

    private lateinit var mainActivity: MainActivity

    private val recyclerViewAdapter =
        MyDepartureRecyclerViewAdapter(clickListener)

    private lateinit var viewModel: DepartureViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        mainActivity.setOnHeadlineSelectedListener(this)
        return inflater.inflate(R.layout.fragment_departure_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(DepartureViewModel::class.java)
        viewModel.observableDepartureList.observe(this, Observer { departures ->
            departures?.let { recyclerViewAdapter.updateList(departures) } })

        departureList.adapter = recyclerViewAdapter
        val decoration = DividerItemDecoration(context, HORIZONTAL)
        departureList.addItemDecoration(decoration)
        addListener()
        addEvents()
    }

    fun refresh() {
        viewModel.refresh()
    }

    private fun onFlightClicked(item: Departure) {
        val flightDetails = actionTabsFragment2ToDetailsFragment()
        flightDetails.flightId = item.id
        flightDetails.flightType = FlightType.DEPARTURE.type
        view?.let {
            findNavController(it).navigate(flightDetails)
        }
    }


    private fun addListener() {
        swipeRefreshDepartureLayout.setOnRefreshListener {
            recyclerViewAdapter.clearList()
            recyclerViewAdapter.notifyDataSetChanged()
            viewModel.refresh()
        }
    }

    private fun addEvents() {
        viewModel.getMainFlow().observe(this, Observer { handleWithMainFlow(it) })
    }

    private fun handleWithMainFlow(flowState: FlowState<MutableList<Departure>>?){
        when(flowState?.status){
            FlowStatus.LOADING -> swipeRefreshDepartureLayout.isRefreshing = true
            FlowStatus.SUCCESS -> swipeRefreshDepartureLayout.isRefreshing = false
            else -> {}
        }
    }

    override fun onStop() {
        viewModel.getMainFlow().removeObservers(this)
        super.onStop()
    }

    override fun onFragmentRefresh() {
        refresh()
    }
}
