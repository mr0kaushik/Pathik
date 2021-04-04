package com.pathik.ride.ui.forget

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuthException
import com.pathik.ride.R
import com.pathik.ride.databinding.FragmentForgetPasswordBinding
import com.pathik.ride.network.Resource
import com.pathik.ride.ui.login.LoginFragmentDirections
import com.pathik.ride.utils.Util
import com.pathik.ride.utils.getProgressDialog
import com.pathik.ride.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ForgetPasswordFragment : Fragment(R.layout.fragment_forget_password) {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentForgetPasswordBinding

    private val viewModel by viewModels<ForgetPasswordViewModel>()
    private var progressDialog: AlertDialog? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentForgetPasswordBinding.bind(view)
        navController = view.findNavController()

        binding.etEmail.addTextChangedListener(forgetPasswordTextWatcher)

        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }

        binding.btnGetRecoveryEmail.setOnClickListener {
            if (validateForm()) {
                viewModel.resetEmail(binding.etEmail.text.toString())
            }
        }

        viewModel.resetMailSent.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    Timber.i("Show Dialog")
                    progressDialog =
                        requireContext().getProgressDialog(R.string.logging_in).show()
                }
                is Resource.Success -> {
                    progressDialog?.hide()
                    lifecycleScope.launch {
                        Timber.i("User Logged In");
                        binding.root.snackbar("Reset email sent")
                        navController.navigate(LoginFragmentDirections.actionLoginFragmentToMainActivity())
                    }
                }
                is Resource.Failure -> {
                    progressDialog?.hide()
                    Timber.e(it.exception)
                    if (it.exception is FirebaseAuthException) {
                        binding.root.snackbar(getString(Util.getSimpleErrorResourceId(it.exception)))
                    } else {
                        binding.root.snackbar("An error occurred !!")
                    }
                }
            }
        })
    }

    private fun validateForm(): Boolean {

        val email = binding.etEmail
            .text
            .toString()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Enter valid email address"
            return false
        }

        return true;
    }


    private val forgetPasswordTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val email: String = binding.etEmail.text
                .toString()
                .trim()
            resetErrors();
            binding.btnGetRecoveryEmail.isEnabled = email.isNotEmpty()
        }
    }

    private fun resetErrors() {
        binding.tilEmail.error = null
        binding.tilEmail.isErrorEnabled = false
    }


}