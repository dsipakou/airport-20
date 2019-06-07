package com.example.airport20.presentation.flightdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.example.airport20.R
import com.example.airport20.domain.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {

    private lateinit var viewModel: DetailsViewModel

    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.getItem(0).isVisible = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        viewModel.setFlight(args.flightId, FlightType.fromInt(args.flightType)!!)
        viewModel.observableFlight.observe(this, Observer { flight ->
            flight?.let { render(flight) } ?: renderNoteNotFound()
        })
    }

    fun render(flight: Flight) {
        codeTextView.text = flight.code
        cityTextView.text = flight.city
        if (!flight.imageUrl.isEmpty()) {
            Picasso.get().load(flight.imageUrl).into(coverImageView)
        }
        if (!flight.companyUrl.isEmpty()) {
            Picasso.get().load(flight.companyUrl).into(aircompanyLogoImageView)
            aircompanyTextView.visibility = GONE
            aircompanyLogoImageView.visibility = VISIBLE
        } else {
            aircompanyTextView.text = flight.company
            aircompanyLogoImageView.visibility = GONE
            aircompanyTextView.visibility = VISIBLE
        }
        if (flight.status != Status.EMPTY && flight.status != Status.UNKNOWN) {
            statusTextView.visibility = VISIBLE
            statusTextView.text = view!!.resources.getString(flight.status.item)
        } else {
            statusTextView.visibility = GONE
        }
        if (!flight.airport.isEmpty()) {
            airportTextView.text = flight.airport
            airportTextView.visibility = VISIBLE
        } else {
            airportTextView.visibility = GONE
        }

        gateTextView.text = flight.gate
        expTimeTextView.text = flight.expectedTime
        if (flight.actualTime.isEmpty()) {
            actTimeHeaderTextView.visibility = GONE
        } else {
            actTimeHeaderTextView.visibility = VISIBLE
        }
        actTimeTextView.text = flight.actualTime
    }

//    override fun onResume() {
//        super.onResume()
//        codeTextView.text = args.flightId.toString()
//    }

    private fun renderNoteNotFound() {
        cityTextView.text = "Not found"
    }
}
