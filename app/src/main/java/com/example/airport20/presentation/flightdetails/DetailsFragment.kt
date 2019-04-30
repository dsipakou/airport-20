package com.example.airport20.presentation.flightdetails

import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.example.airport20.R
import com.example.airport20.domain.*
import com.google.android.gms.tasks.Tasks
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
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
        return inflater.inflate(R.layout.fragment_details, container, false)
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
        gateTextView.text = flight.gate
        expTimeTextView.text = flight.expectedTime
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
