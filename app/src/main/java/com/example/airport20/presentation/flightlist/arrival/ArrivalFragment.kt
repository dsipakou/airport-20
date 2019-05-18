package com.example.airport20.presentation.flightlist.arrival

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import com.example.airport20.R
import com.example.airport20.domain.Arrival
import com.example.airport20.domain.FlightType

import com.example.airport20.presentation.flighttabs.TabsFragmentDirections.actionTabsFragment2ToDetailsFragment
import com.example.airport20.utils.FlowState
import com.example.airport20.utils.FlowStatus
import kotlinx.android.synthetic.main.fragment_arrival_list.*


class ArrivalFragment : Fragment() {

    private val clickListener: ArrivalClickListener = this::onFlightClicked

    lateinit var recyclerViewAdapter: MyArrivalRecyclerViewAdapter

    private lateinit var viewModel: ArrivalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_arrival_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewAdapter = MyArrivalRecyclerViewAdapter(clickListener)

        viewModel = ViewModelProviders.of(this).get(ArrivalViewModel::class.java)
        viewModel.observableArrivalList.observe(this, Observer { arrivals ->
            arrivals?.let { recyclerViewAdapter.updateList(arrivals) }
        })
        arrivalList.adapter = recyclerViewAdapter
        addListener()
        addEvents()
    }

    private fun onFlightClicked(item: Arrival) {
        val flightDetails = actionTabsFragment2ToDetailsFragment()
        flightDetails.flightId = item.id
        flightDetails.flightType = FlightType.ARRIVAL.type
        view?.let {
            findNavController(it).navigate(flightDetails)
        }
    }

    private fun addListener() {
        swipeRefreshArrivalLayout.setOnRefreshListener {
            recyclerViewAdapter.clearList()
            recyclerViewAdapter.notifyDataSetChanged()
            viewModel.refresh()
        }
    }

    private fun addEvents() {
        viewModel.getMainFlow().observe(this, Observer { handleWithMainFlow(it) })
    }

    private fun handleWithMainFlow(flowState: FlowState<MutableList<Arrival>>?){
        when(flowState?.status){
            FlowStatus.LOADING -> swipeRefreshArrivalLayout.isRefreshing = true
            FlowStatus.SUCCESS -> swipeRefreshArrivalLayout.isRefreshing = false
            else -> {}
        }
    }
}
