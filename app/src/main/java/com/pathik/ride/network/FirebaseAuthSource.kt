package com.pathik.ride.network

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.pathik.ride.model.User
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class FirebaseAuthSource @Inject constructor(
    var firebaseAuth: FirebaseAuth,
    var firebaseFirestore: FirebaseFirestore
) {

    var currentUserId = firebaseAuth.currentUser?.uid;

    var currentUser: FirebaseUser? = firebaseAuth.currentUser;

    suspend fun register(user: User, password: String): FirebaseUser? {
//        try {
//
//            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
//                .await().let {
//                    user.userId = it?.user?.uid ?: ""
//                    return try {
//                        firebaseFirestore.collection(Constants.COLLECTION_USERS)
//                            .document(user.userId)
//                            .set(user.map)
//                            .await()
//                        it?.user
//                    } catch (e: Exception) {
//                        Timber.e(e)
//                        it?.user
//                    }
//                }
//
//        } catch (e: Exception) {
//            Timber.e(e)
//            return null
//        }

        return null
    }

    suspend fun login(email: String, password: String): FirebaseUser ? {
//        val _user: MutableLiveData<Resource<FirebaseUser>> = MutableLiveData()
//
//
//        _user.value = Resource.Loading
//
//        firebaseAuth.signInWithEmailAndPassword(email, password).await()
//        try {
//            firebaseAuth.signInWithEmailAndPassword(email, password).await()
//
//        } catch (e: Exception) {
//            Timber.e(e)
//            null
//        }
        return null
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}