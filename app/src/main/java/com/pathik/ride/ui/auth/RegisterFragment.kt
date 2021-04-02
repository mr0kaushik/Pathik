package com.pathik.ride.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.pathik.ride.R
import com.pathik.ride.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentRegisterBinding.bind(view);
        navController = view.findNavController();

        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }

        binding.btnSignIn.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            navController.navigate(action)
        }

        binding.btnRegister.setOnClickListener {
            // TODO ADD FIREBASE REGISTER
        }
    }
}