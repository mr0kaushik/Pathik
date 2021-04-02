package com.pathik.ride.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.pathik.ride.R
import com.pathik.ride.databinding.FragmentForgetPasswordBinding

class ForgetPasswordFragment : Fragment(R.layout.fragment_forget_password) {

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentForgetPasswordBinding.bind(view)

        navController = view.findNavController()

        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }

        binding.btnGetRecoveryEmail.setOnClickListener {

        }
    }
}