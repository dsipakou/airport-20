package com.example.airport20.presentation.flighttabs


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager

import com.example.airport20.R
import com.example.airport20.presentation.flightlist.arrival.ArrivalFragment
import com.example.airport20.presentation.flightlist.departure.DepartureFragment
import com.google.android.material.tabs.TabLayout


class TabsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tabs, container, false)
        val viewPager = view.findViewById<ViewPager>(R.id.viewPager)
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        val tabsAdapter = ViewPagerAdapter(childFragmentManager)
        tabsAdapter.addFragment(DepartureFragment(), getString(R.string.departure))
        tabsAdapter.addFragment(ArrivalFragment(), getString(R.string.arrival))
        viewPager.adapter = tabsAdapter
        tabLayout.setupWithViewPager(viewPager)
        return view
    }
}
