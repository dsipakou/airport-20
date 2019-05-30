package com.example.airport20.presentation.settings

import android.os.Bundle
import android.view.Menu
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.airport20.MainActivity

import com.example.airport20.R
import com.example.airport20.domain.FlightManager

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var onChangeLanguage: MainActivity

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val pref = findPreference(getString(R.string.pref_key_language))
        pref.setOnPreferenceChangeListener { preference, newValue ->
            if (preference is ListPreference) {
                onChangeLanguage = activity as MainActivity
                onChangeLanguage.setLanguage(newValue.toString())
            }
            true
        }
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        val mainActivity: MainActivity = activity as MainActivity
        mainActivity.setActivityBarTitle(R.string.settings)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.getItem(0).isVisible = false
    }
}
