package com.pathik.ride.ui.fragments.forget

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pathik.ride.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel
@Inject
constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    fun forgetPassword(email: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

}