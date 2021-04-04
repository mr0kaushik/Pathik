package com.pathik.ride.utils

import android.content.Context
import com.pathik.ride.PathikApp

object UserPref {

    const val PREF_NAME = "pathik_pref"

    const val KEY_USER_ID = "user_id"
    const val KEY_NAME = "user_name"

    private val pref = PathikApp.context()
        .getSharedPreferences("pathik_pref", Context.MODE_PRIVATE)

    fun putString(key: String, value: String) {
        pref.edit().putString("user_id", value).apply()
    }

    fun getString(key: String): String {
        return pref.getString(key, "") ?: ""
    }

}