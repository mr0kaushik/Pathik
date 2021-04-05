package com.pathik.ride.network

import android.graphics.Bitmap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.pathik.ride.model.User
import com.pathik.ride.model.User.Companion.toUser
import com.pathik.ride.utils.Util
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDataSource @Inject constructor(
    val firestore: FirebaseFirestore,
    val storageReference: StorageReference,
) {

    suspend fun getAllSavedCards(userId: String): Resource<List<Any>> {
        return Resource.Success(
            firestore.collection(Constants.COLLECTION_USERS)
                .document(userId)
                .collection(Constants.COLLECTION_SAVED_CARDS)
                .get().await().documents
        )
    }

    suspend fun fetchUserInfo(userId: String): Resource<User> {
        return Resource.Success(
            firestore.collection(Constants.COLLECTION_USERS)
                .document(userId).get().await()
                .toUser()
        )
    }

    suspend fun setUserInfo(userId: String, map: HashMap<String, Any?>): Resource<Boolean> {
        firestore.collection(Constants.COLLECTION_USERS)
            .document(userId)
            .set(map)
            .await()
        return Resource.Success(true)
    }

    suspend fun uploadProfilePicture(userId: String, bitmap: Bitmap): Resource<Boolean> {
        val profileReference = storageReference.child(Constants.PROFILES_STORAGE)
            .child("$userId.png")
        val it = profileReference.putBytes(Util.convertImage2ByteArray(bitmap)!!)
        val task = profileReference.downloadUrl.await()
        return setUserInfo(userId, hashMapOf("photoUrl" to task.path.toString()))
    }

}