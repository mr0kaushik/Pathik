package com.pathik.ride.ui.fragments.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.pathik.ride.R
import com.pathik.ride.databinding.FragmentProfileBinding
import com.pathik.ride.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}