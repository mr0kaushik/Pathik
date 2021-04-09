package com.pathik.ride.ui.fragments.forget

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
import com.google.firebase.FirebaseException
import com.pathik.ride.R
import com.pathik.ride.databinding.FragmentForgetPasswordBinding
import com.pathik.ride.network.Resource
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
                getRecoveryMail()
            }

        }
    }

    private fun getRecoveryMail() {
        viewModel.forgetPassword(
            binding.etEmail.text.toString(),
        ).observe(
            viewLifecycleOwner, Observer {
                when (it) {
                    is Resource.Loading -> {
                        lifecycleScope.launch {

                            progressDialog =
                                requireContext().getProgressDialog(R.string.sending_recovery_email)
                                    .show()
                        }
                    }
                    is Resource.Success -> {
                        lifecycleScope.launch {
                            progressDialog?.dismiss()
                            binding.root.snackbar(getString(R.string.recovery_email_sent))
                            navController.popBackStack()
                        }
                    }
                    is Resource.Failure -> {
                        lifecycleScope.launch {
                            Timber.e(it.exception)
                            progressDialog?.dismiss()
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

    private fun validateForm(): Boolean {

        val email = binding.etEmail
            .text
            .toString()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.enter_valid_email_address)
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

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
        }
    }


}