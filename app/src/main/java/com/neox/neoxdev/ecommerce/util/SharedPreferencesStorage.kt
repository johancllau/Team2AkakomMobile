package com.neox.neoxdev.ecommerce.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesStorage(context: Context) {
    private var sharedPreferences: SharedPreferences = context.getSharedPreferences(Constanta.PREF_NAME, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    var login: Boolean = false
        get() {
            return sharedPreferences.getBoolean(Constanta.LOGIN, field)
        }

        set(value) {
            editor.putBoolean(Constanta.LOGIN, value)
            editor.apply()
        }


    var userData: String? = ""
        get() {
            return sharedPreferences.getString(Constanta.DATA_USER, field)
        }

        set(value) {
            editor.putString(Constanta.DATA_USER, value)
            editor.apply()
        }

}