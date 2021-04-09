package com.pathik.ride.ui.activities.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pathik.ride.R
import com.pathik.ride.databinding.ActivitySettingsBinding
import com.pathik.ride.databinding.AppBarLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appBarLayoutBinding = AppBarLayoutBinding.bind(binding.root.getChildAt(0))
        appBarLayoutBinding.tvAppBarTitle.text = getString(R.string.setting)
        appBarLayoutBinding.btnBack.setOnClickListener {
            onBackPressed()
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.content, SettingsFragment())
            .commit()
    }


}