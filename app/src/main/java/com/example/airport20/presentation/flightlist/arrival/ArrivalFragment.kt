package com.example.airport20.presentation.flightlist.arrival

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.example.airport20.presentation.flightlist.MyArrivalRecyclerViewAdapter
import com.example.airport20.R

import com.example.airport20.dummy.DummyContent.DummyItem
import com.example.airport20.presentation.flightlist.ClickListener
import com.example.airport20.presentation.flightlist.arrival.ArrivalFragmentDirections.actionArrivalFragmentToDetailsFragment
import kotlinx.android.synthetic.main.fragment_arrival_list.*


class ArrivalFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1

    private val clickListener: ClickListener = this::onFlightClicked

    private val recyclerViewAdapter = MyArrivalRecyclerViewAdapter(clickListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_arrival_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrivalList.adapter = recyclerViewAdapter
    }

    private fun onFlightClicked(item: DummyItem) {
        val flightDetails = actionArrivalFragmentToDetailsFragment()
        flightDetails.flightId = item.id
        view?.let {
            findNavController(it).navigate(flightDetails)
        }
    }
}
