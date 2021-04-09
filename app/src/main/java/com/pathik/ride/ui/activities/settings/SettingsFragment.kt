package com.pathik.ride.ui.activities.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.pathik.ride.R
import com.pathik.ride.network.Resource
import com.pathik.ride.ui.activities.AuthActivity
import com.pathik.ride.ui.fragments.login.LoginViewModel
import com.pathik.ride.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel by viewModels<LoginViewModel>()
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val preference =
            preferenceScreen.findPreference<Preference>(getString(R.string.pref_key_logout))

        preference?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener { //
                // handle click here
                Timber.i(preference.toString())
                viewModel.logout().observe(this) {
                    when (it) {
                        is Resource.Success -> {
                            moveToLogin()
                        }
                        is Resource.Failure -> {
                            view?.snackbar("Failed to logout, please try again later")
                        }
                        Resource.Loading -> {

                        }
                    }
                }
                true
            }
    }

    private fun moveToLogin() {
        startActivity(Intent(requireContext(), AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        requireActivity().finish()
    }
}