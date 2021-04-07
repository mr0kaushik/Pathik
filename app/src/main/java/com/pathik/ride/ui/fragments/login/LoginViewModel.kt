package com.pathik.ride.ui.fragments.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import com.pathik.ride.network.FirebaseDataSource
import com.pathik.ride.network.Resource
import com.pathik.ride.utils.UserPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirebaseDataSource: FirebaseDataSource
) : ViewModel() {


    fun login(email: String, password: String) =
        liveData(Dispatchers.IO) {
            emit(Resource.Loading)
            try {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val user = firebaseFirebaseDataSource.fetchUserInfo(result?.user?.uid!!)
                emit(user)
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }

}