package com.pathik.ride.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pathik.ride.R

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Pathik)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }
}