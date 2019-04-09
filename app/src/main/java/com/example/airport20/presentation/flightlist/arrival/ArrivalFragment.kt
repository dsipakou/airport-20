package com.example.airport20.presentation.flightlist.arrival

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import com.example.airport20.presentation.flightlist.MyArrivalRecyclerViewAdapter
import com.example.airport20.R

import com.example.airport20.dummy.DummyContent
import com.example.airport20.dummy.DummyContent.DummyItem
import com.example.airport20.presentation.flightlist.ClickListener
import com.example.airport20.presentation.flightlist.arrival.ArrivalFragmentDirections.actionArrivalFragmentToDetailsFragment
import kotlinx.android.synthetic.main.fragment_arrival_list.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ArrivalFragment.OnListFragmentInteractionListener] interface.
 */
class ArrivalFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    private val clickListener: ClickListener = this::onNoteClicked

    private val recyclerViewAdapter = MyArrivalRecyclerViewAdapter(clickListener)

    fun setOnListFragmentInteractionListener(callback: OnListFragmentInteractionListener) {
        listener = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: DummyItem?)
    }

    private fun onNoteClicked(item: DummyItem) {
        val navDirections = actionArrivalFragmentToDetailsFragment()
        view?.let {
            findNavController(it).navigate(navDirections)
        }
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            ArrivalFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
