package com.example.airport20

import android.content.Context
import android.os.Bundle
import android.util.Log

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.airport20.domain.FlightManager
import com.example.airport20.domain.TimeRange
import com.example.airport20.utils.LocalHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity() {
    internal lateinit var arrivalRefresh: OnFragmentRecyclerRefresh
    internal lateinit var departureRefresh: OnFragmentRecyclerRefresh

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
        val actionBar: ActionBar? = supportActionBar
        setupNavigation()
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.now_timetable -> {
                    FlightManager.setPeriod(TimeRange.NOW)
                    actionBar?.setTitle(R.string.now)
                    refresh()
                }
                R.id.yesterday_timetable -> {
                    FlightManager.setPeriod(TimeRange.YESTERDAY)
                    actionBar?.setTitle(R.string.yesterday)
                    refresh()
                }
                R.id.today_timetable -> {
                    FlightManager.setPeriod(TimeRange.TODAY)
                    actionBar?.setTitle(R.string.today)
                    refresh()
                }
                R.id.tomorrow_timetable -> {
                    FlightManager.setPeriod(TimeRange.TOMORROW)
                    actionBar?.setTitle(R.string.tomorrow)
                    refresh()
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

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        // Handle navigation view item clicks here.
//        when (item.itemId) {
//            R.id.now_timetable -> {
//
//            }
//            R.id.today_timetable -> {
//                // Handle the camera action
//            }
//            R.id.yesterday_timetable -> {
//
//            }
//            R.id.tomorrow_timetable -> {
//
//            }
//        }
//        return true
//    }

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
        val navController = findNavController(this, R.id.myNavHostFragment)

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
