package com.pathik.ride.ui.fragments.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.firebase.FirebaseException
import com.pathik.ride.R
import com.pathik.ride.databinding.FragmentLoginBinding
import com.pathik.ride.network.Resource
import com.pathik.ride.utils.Util
import com.pathik.ride.utils.getProgressDialog
import com.pathik.ride.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentLoginBinding

    private val loginViewModel by viewModels<LoginViewModel>()

    private var progressDialog: AlertDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        navController = view.findNavController()

        binding.etLoginEmail.addTextChangedListener(loginTextWatcher)
        binding.etLoginPassword.addTextChangedListener(loginTextWatcher)

        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }

        binding.btnForget.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToForgetPasswordFragment()
            navController.navigate(action)
        }

        binding.btnRegister.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            navController.navigate(action)
        }


        binding.btnSignIn.setOnClickListener {
            if (validateForm()) {
                login()
            }
        }


    }

    private fun login() {
        loginViewModel.login(
            binding.etLoginEmail.text.toString(),
            binding.etLoginPassword.text.toString()
        ).observe(
            viewLifecycleOwner, {
                when (it) {
                    is Resource.Loading -> {
                        lifecycleScope.launch {
                            progressDialog =
                                requireContext().getProgressDialog(R.string.logging_in).show()
                        }
                    }

                    is Resource.Success -> {
                        lifecycleScope.launch {
                            progressDialog?.dismiss()
                            binding.root.snackbar(getString(R.string.welcome_back, it.value.name))
                            navController.navigate(LoginFragmentDirections.actionLoginFragmentToMainActivity())
                            requireActivity().finishAffinity()
                        }
                    }
                    is Resource.Failure -> {
                        lifecycleScope.launch {
                            progressDialog?.dismiss()
                            Timber.e(it.exception)
                            if (it.exception is FirebaseException) {
                                binding.root.snackbar(getString(Util.getSimpleErrorResourceId(it.exception)))
                            } else {
                                binding.root.snackbar(getString(R.string.an_error_occur))
                            }
                        }
                    }
                }
            }
        )
    }


    private val loginTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val email: String = binding.etLoginEmail.text
                .toString()
                .trim()
            val password: String = binding.etLoginPassword.text
                .toString()
                .trim()
            resetErrors()
            binding.btnSignIn.isEnabled = email.isNotEmpty() && password.isNotEmpty()
        }
    }

    private fun validateForm(): Boolean {

        val email = binding.etLoginEmail
            .text
            .toString()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Enter valid email address"
            return false
        }

        return true;
    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
        }
    }


    private fun resetErrors() {
        binding.tilEmail.error = null
        binding.tilEmail.isErrorEnabled = false
    }

}