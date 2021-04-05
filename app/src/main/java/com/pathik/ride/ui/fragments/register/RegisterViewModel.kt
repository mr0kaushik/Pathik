package com.pathik.ride.ui.fragments.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.pathik.ride.model.User
import com.pathik.ride.network.FirebaseDataSource
import com.pathik.ride.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel
@Inject
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDataSource: FirebaseDataSource
) : ViewModel() {

    fun register(user: User, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(user.email!!, password).await()
            user.userId = result?.user?.uid!!
            user.createdAt = Timestamp.now()
            user.updatedAt = Timestamp.now()
            firebaseDataSource.setUserInfo(result.user?.uid!!, user.toMap)
            emit(Resource.Success(user))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

}