package com.example.airport20.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.res.Configuration
import java.util.*


class LocalHelper {
    fun onAttach(context: Context): Context {
        val language: String = Locale.getDefault().language
        val prefs = context.getSharedPreferences("settings", MODE_PRIVATE)
        val lang = prefs.getString("msq_lang", "")
        if (lang != null && lang.isNotEmpty()) {
            return setLocal(context, lang)
        }
        return setLocal(context, language)
    }

    fun setLocal(context: Context, language: String): Context {
        return upgradeResource(context, language)
    }

    private fun upgradeResource(context: Context, language: String): Context {
        val local = Locale(language)
        val configuration: Configuration = context.resources.configuration

        Locale.setDefault(local)
        configuration.setLocale(local)

        val prefs = context.getSharedPreferences("settings", MODE_PRIVATE).edit()
        prefs.putString("msq_lang", language)
        prefs.commit()
        return context.createConfigurationContext(configuration)
    }
}