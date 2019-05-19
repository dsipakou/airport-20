package com.example.airport20

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.airport20.presentation.flightlist.arrival.ArrivalViewModel
import com.example.airport20.presentation.flightlist.departure.DepartureViewModel
import com.example.airport20.utils.LocalHelper
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupNavigation()
//        navigationView.setNavigationItemSelectedListener {
//            Log.i("Inside navigation", "Listener is working")
////            when(it.itemId) {
////                R.id.now_timetable -> {
////                    Log.i("drawer menu click", "now_timetable")
////                }
////            }
//            it.isChecked = true
//            drawer_layout.closeDrawers()
//            true
//        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocalHelper().onAttach(newBase))
    }

    fun setLanguage(language: String) {
        LocalHelper().setLocal(this, language)
        this.recreate()
        ArrivalViewModel().refresh()
        DepartureViewModel().refresh()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
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
}
