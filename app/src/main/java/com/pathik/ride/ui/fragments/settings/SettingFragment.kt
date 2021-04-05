package com.pathik.ride.ui.fragments.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.pathik.ride.R
import com.pathik.ride.databinding.FragmentRegisterBinding
import com.pathik.ride.databinding.FragmentSettingsBinding
import com.pathik.ride.databinding.FragmentWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment(R.layout.fragment_settings) {


    private lateinit var binding: FragmentSettingsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}