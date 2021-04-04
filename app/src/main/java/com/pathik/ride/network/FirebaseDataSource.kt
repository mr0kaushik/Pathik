package com.pathik.ride.network

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.pathik.ride.model.User
import com.pathik.ride.model.User.Companion.toUser
import com.pathik.ride.utils.Util
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class FirebaseDataSource @Inject constructor(
    val firebaseAuthSource: FirebaseAuthSource,
    val firestore: FirebaseFirestore,
    val storageReference: StorageReference,
) {

    suspend fun getAllSavedCards(userId: String): List<Any> {
        return try {
            firestore.collection(Constants.COLLECTION_USERS)
                .document(firebaseAuthSource.currentUserId!!)
                .collection(Constants.COLLECTION_SAVED_CARDS)
                .get().await().toObjects(Any::class.java)
        } catch (e: Exception) {
            emptyList<Any>()
        }
    }

    suspend fun fetchUserInfo(): User? {
        return try {
            firestore.collection(Constants.COLLECTION_USERS)
                .document(firebaseAuthSource.currentUserId!!).get().await()
                .toUser()
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }

    suspend fun setUserInfo(user: User): LiveData<Resource<Boolean>> {
        val uploadState = MutableLiveData<Resource<Boolean>>()
        uploadState.value = Resource.Loading
        try {
            firestore.collection(Constants.COLLECTION_USERS)
                .document(firebaseAuthSource.currentUserId!!)
                .set(user.map)
                .await()
            uploadState.value = Resource.Success(true)
        } catch (e: Exception) {
            Timber.e(e)
            uploadState.value = Resource.Failure(e)
        }
        return uploadState
    }

    suspend fun uploadProfilePicture(bitmap: Bitmap): LiveData<Resource<Boolean>> {
        val uploadState = MutableLiveData<Resource<Boolean>>()
        uploadState.value = Resource.Loading
        try {
            val profileReference = storageReference.child(Constants.PROFILES_STORAGE)
                .child(firebaseAuthSource.currentUserId + ".png")
            val it = profileReference.putBytes(Util.convertImage2ByteArray(bitmap)!!)
                .await()

            val task = profileReference.downloadUrl.await().also {
                firestore.collection(Constants.COLLECTION_USERS)
                    .document(firebaseAuthSource.currentUserId!!)
                    .set(hashMapOf<String, Any>("photoUrl" to it.toString()))
            }


            uploadState.value = Resource.Success(true)

        } catch (e: Exception) {
            Timber.e(e)
            uploadState.value = Resource.Failure(e)
        }
        return uploadState
    }

}