package com.pathik.ride.ui.activities.profile

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import com.pathik.ride.network.FirebaseDataSource
import com.pathik.ride.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirebaseDataSource: FirebaseDataSource
) : ViewModel() {

    fun fetchUserData() =
        liveData(Dispatchers.IO) {
            emit(Resource.Loading)
            try {
                val user = firebaseFirebaseDataSource.fetchUserInfo(firebaseAuth.currentUser?.uid!!)
                emit(user)
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }


    fun saveUserData(bitmap: Bitmap? = null, data: HashMap<String, Any>) =
        liveData(Dispatchers.IO) {
            emit(Resource.Loading)
            try {
                val uId = firebaseAuth.currentUser?.uid!!
                firebaseFirebaseDataSource.setProfileWithData(uId, bitmap, data)
                emit(firebaseFirebaseDataSource.fetchUserInfo(uId))
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }


}