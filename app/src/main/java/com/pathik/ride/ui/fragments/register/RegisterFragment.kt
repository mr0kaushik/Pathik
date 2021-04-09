package com.pathik.ride.ui.fragments.register

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
import com.google.firebase.Timestamp
import com.pathik.ride.R
import com.pathik.ride.databinding.FragmentRegisterBinding
import com.pathik.ride.model.User
import com.pathik.ride.network.Resource
import com.pathik.ride.utils.Util
import com.pathik.ride.utils.getProgressDialog
import com.pathik.ride.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentRegisterBinding

    private val viewModel by viewModels<RegisterViewModel>()

    private var progressDialog: AlertDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()

        binding = FragmentRegisterBinding.bind(view)

        binding.etName.addTextChangedListener(signUpTextWatcher)
        binding.etPhone.addTextChangedListener(signUpTextWatcher)
        binding.etEmail.addTextChangedListener(signUpTextWatcher)
        binding.etPassword.addTextChangedListener(signUpTextWatcher)
        binding.etConfirmPassword.addTextChangedListener(signUpTextWatcher)

        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }

        binding.btnSignIn.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            navController.navigate(action)
        }

        binding.btnRegister.setOnClickListener {
            val user = validateAllFields()
            if (user != null) {
                register(user)
            }
        }
    }

    private fun register(user: User) {
        viewModel.register(user, binding.etPassword.text.toString())
            .observe(viewLifecycleOwner, {
                when (it) {
                    is Resource.Loading -> {
                        lifecycleScope.launch {
                            progressDialog =
                                requireContext().getProgressDialog(R.string.signing_up).show()
                        }
                    }
                    is Resource.Success -> {
                        lifecycleScope.launch {
                            progressDialog?.dismiss()
                            binding.root.snackbar(getString(R.string.successfully_registerd))
                            navController.navigate(RegisterFragmentDirections.actionRegisterFragmentToMainActivity())
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
                                binding.root.snackbar(getString(R.string.common_auth_error))
                            }
                        }
                    }
                }
            })
    }

    private val signUpTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val name: String = binding.etName.text
                .toString()
                .trim()

            val phone: String = binding.etPhone.text
                .toString()
                .trim()

            val email: String = binding.etEmail.text
                .toString()
                .trim()

            val password: String = binding.etPassword.text
                .toString()
                .trim()

            val confirmPassword: String = binding.etConfirmPassword.text
                .toString()
                .trim()

            resetErrors()
            binding.btnRegister.isEnabled = name.isNotEmpty()
                    && phone.isNotEmpty()
                    && email.isNotEmpty()
                    && password.isNotEmpty()
                    && confirmPassword.isNotEmpty()
        }
    }

    private fun validateAllFields(): User? {
        val name = binding.etName
            .text
            .toString()

        val phone = binding.etPhone
            .text
            .toString()

        if (!Patterns.PHONE.matcher(phone).matches() || phone.length != 10) {
            binding.tilPhoneNumber.error = getString(R.string.enter_valid) + "phone number"
            return null
        }

        val email = binding.etEmail
            .text
            .toString()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.enter_valid) + "email address"
            return null
        }

        val password = binding.etPassword
            .text
            .toString()

        val confirmPassword = binding.etConfirmPassword
            .text
            .toString()

        if (password != confirmPassword) {
            binding.tilConfirmPassword.error = getString(R.string.password_not_matched)
            return null
        }

        return User(name = name, createdAt = Timestamp.now(), email = email, phoneNumber = phone)
    }

    private fun resetErrors() {
        binding.tilPhoneNumber.error = null
        binding.tilPhoneNumber.isErrorEnabled = false
        binding.tilEmail.error = null
        binding.tilEmail.isErrorEnabled = false
        binding.tilConfirmPassword.error = null
        binding.tilConfirmPassword.isErrorEnabled = false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
        }
    }

}