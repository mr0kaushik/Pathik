package com.pathik.ride.utils

import android.content.Context
import com.pathik.ride.PathikApp

object UserPref {

    const val PREF_NAME = "pathik_pref"

    //    const val KEY_USER_ID = "user_id"
    const val KEY_NAME = "user_name"
    const val KEY_EMAIL = "user_email"
    const val KEY_USER_PROFILE_URL = "user_profile_url"

    private val pref = PathikApp.context()
        .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun putString(key: String, value: String) {
        pref.edit().putString(key, value).apply()
    }

    fun getString(key: String): String {
        return pref.getString(key, "") ?: ""
    }

    fun clearAll() {
        return pref.edit().clear().apply()
    }


}