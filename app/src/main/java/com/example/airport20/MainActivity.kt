package com.example.airport20

import android.content.Context
import android.os.Bundle

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.airport20.domain.FlightManager
import com.example.airport20.domain.TimeRange
import com.example.airport20.presentation.flighttabs.TabsFragmentDirections.actionTabsFragment2ToSettingsFragment
import com.example.airport20.utils.LocalHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity() {
    internal lateinit var arrivalRefresh: OnFragmentRecyclerRefresh
    internal lateinit var departureRefresh: OnFragmentRecyclerRefresh
    internal lateinit var navController: NavController
    internal lateinit var actionBar: ActionBar

    fun setOnArrivalRefresh(arrival: OnFragmentRecyclerRefresh) {
        this.arrivalRefresh = arrival
    }

    fun setOnDepartureRefresh(departure: OnFragmentRecyclerRefresh) {
        this.departureRefresh = departure
    }

    fun getOnArrivalRefresh(): OnFragmentRecyclerRefresh {
        return arrivalRefresh
    }

    fun getOnDepartureRefresh(): OnFragmentRecyclerRefresh {
        return departureRefresh
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        actionBar = supportActionBar!!
        setupNavigation()
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.now_timetable -> {
                    FlightManager.setPeriod(TimeRange.NOW)
                    setActivityBarTitle()
                    refresh()
                }
                R.id.yesterday_timetable -> {
                    FlightManager.setPeriod(TimeRange.YESTERDAY)
                    setActivityBarTitle()
                    refresh()
                }
                R.id.today_timetable -> {
                    FlightManager.setPeriod(TimeRange.TODAY)
                    setActivityBarTitle()
                    refresh()
                }
                R.id.tomorrow_timetable -> {
                    FlightManager.setPeriod(TimeRange.TOMORROW)
                    setActivityBarTitle()
                    refresh()
                }
                R.id.settingsFragment -> {
                    navController.navigate(actionTabsFragment2ToSettingsFragment())
                }
            }
            it.isChecked = true
            drawer_layout.closeDrawers()
            true
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocalHelper().onAttach(newBase))
    }

    fun setLanguage(language: String) {
        LocalHelper().setLocal(this, language)
        this.recreate()
        refresh()
    }

    fun setActivityBarTitle() {
        when (FlightManager.getPeriod()) {
            TimeRange.NOW -> setActivityBarTitle(R.string.now)
            TimeRange.TOMORROW -> setActivityBarTitle(R.string.tomorrow)
            TimeRange.YESTERDAY -> setActivityBarTitle(R.string.yesterday)
            TimeRange.TODAY -> setActivityBarTitle(R.string.today)
        }
    }

    fun setActivityBarTitle(title: Int) {
        actionBar.setTitle(title)
    }

    private fun refresh() {
        getOnArrivalRefresh().onFragmentRefresh()
        getOnDepartureRefresh().onFragmentRefresh()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.refresh, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> refresh()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigateUp(findNavController(this, R.id.myNavHostFragment), drawer_layout)
    }

    private fun setupNavigation() {
        navController = findNavController(this, R.id.myNavHostFragment)

        // Update action bar to reflect navigation
        setupActionBarWithNavController(this, navController, drawer_layout)

//        // Handle nav drawer item clicks
//        navigationView.setNavigationItemSelectedListener {
//            Log.i("Inside navigation", "Listener is working")
//            when(it.itemId) {
//                R.id.now_timetable -> {
//                    Log.i("drawer menu click", "now_timetable")
//                }
//            }
//            it.isChecked = true
//            drawer_layout.closeDrawers()
//            true
//        }
//        navigationView.setNavigationItemSelectedListener {
//            drawer_layout.closeDrawer(GravityCompat.START)
//            if (it.isChecked) {
//                return@setNavigationItemSelectedListener false
//            } else {
//                return@setNavigationItemSelectedListener true
//            }
//        }

        // Tie nav graph to items in nav drawer
        setupWithNavController(navigationView, navController)
    }

    interface OnFragmentRecyclerRefresh {
        fun onFragmentRefresh()
    }
}
