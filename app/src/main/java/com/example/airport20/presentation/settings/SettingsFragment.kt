package com.example.airport20.presentation.settings


import android.os.Bundle
import android.util.Log
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat

import com.example.airport20.R


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val pref = findPreference(getString(R.string.pref_key_language))
        pref.setOnPreferenceChangeListener { preference, newValue ->
            if (preference is ListPreference) {
                Log.i("Preferences", newValue.toString())
            }
            true
        }
    }
}
