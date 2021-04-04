package com.pathik.ride.ui.forget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pathik.ride.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel
@Inject
constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _resetMailSent: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val resetMailSent: LiveData<Resource<Boolean>>
        get() = _resetMailSent

    fun resetEmail(email: String) = viewModelScope.launch {
        _resetMailSent.value = Resource.Loading
        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener {
            _resetMailSent.value = Resource.Success(true)
        }.addOnFailureListener {
            _resetMailSent.value = Resource.Failure(it)
        }
    }

}