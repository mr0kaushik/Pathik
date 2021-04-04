package com.pathik.ride.ui.login

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
class LoginViewModel
@Inject
constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _user: MutableLiveData<Resource<FirebaseUser>> = MutableLiveData()
    val currentUser: LiveData<Resource<FirebaseUser>>
        get() = _user

    fun login(email: String, password: String) = viewModelScope.launch {
        _user.value = Resource.Loading
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            _user.value = Resource.Success(it.user!!)
        }.addOnFailureListener {
            _user.value = Resource.Failure(it)
        }
    }
}