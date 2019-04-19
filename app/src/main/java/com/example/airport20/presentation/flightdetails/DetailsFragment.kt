package com.example.airport20.presentation.flightdetails

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.example.airport20.R
import com.example.airport20.domain.Arrival
import com.example.airport20.domain.Flight
import com.example.airport20.domain.FlightManager
import com.example.airport20.domain.FlightType
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_details.*


class DetailsFragment : Fragment() {

    private lateinit var viewModel: DetailsViewModel

    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        viewModel.setFlight(args.flightId, FlightType.fromInt(args.flightType)!!)
        viewModel.observableFlight.observe(this, Observer { flight ->
            flight?.let { render(flight) } ?: renderNoteNotFound() })
    }

    fun render(flight: Flight) {
        codeTextView.text = flight.code
        cityTextView.text = flight.city
        gateTextView.text = "hheeeeeeeooollo"
    }

//    override fun onResume() {
//        super.onResume()
//        codeTextView.text = args.flightId.toString()
//    }

    private fun renderNoteNotFound() {
        codeTextView.text = "heeeelllllooo"
    }
}
