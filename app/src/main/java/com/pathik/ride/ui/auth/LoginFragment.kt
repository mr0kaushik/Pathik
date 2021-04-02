package com.pathik.ride.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.pathik.ride.R
import com.pathik.ride.databinding.FragmentLoginBinding
import com.pathik.ride.databinding.FragmentWelcomeBinding

class LoginFragment: Fragment(R.layout.fragment_login) {

    private lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLoginBinding.bind(view)
        navController = view.findNavController()

        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }

        binding.btnSignIn.setOnClickListener {
            // TODO Add Login

        }
        binding.btnForget.setOnClickListener{

            val action = LoginFragmentDirections.actionLoginFragmentToForgetPasswordFragment()
            navController.navigate(action)
        }
        binding.btnRegister.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            navController.navigate(action)
        }
    }
}