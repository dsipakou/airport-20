package com.example.airport20.presentation.settings


import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

import com.example.airport20.R


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}
