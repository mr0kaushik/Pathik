package com.pathik.ride.ui.activities.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pathik.ride.R
import com.pathik.ride.databinding.ActivitySettingsBinding
import timber.log.Timber

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        Timber.i("on resume")
    }

    override fun onStart() {
        super.onStart()
        Timber.i("On Start")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("ON Stop")
    }


}