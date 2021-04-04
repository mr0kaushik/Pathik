package com.pathik.ride.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.pathik.ride.R
import com.pathik.ride.databinding.FragmentWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentWelcomeBinding.bind(view)

        binding.btnRegister.setOnClickListener {
            Timber.i("Btn Register Call");
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToRegisterFragment();
            view.findNavController().navigate(action)
        }

        binding.btnSignIn.setOnClickListener {
            Timber.i("Btn Sign In Call")
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment();
            view.findNavController().navigate(action)
        }

    }


}