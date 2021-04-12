package com.pathik.ride.network

import android.graphics.Bitmap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.StorageReference
import com.pathik.ride.model.User
import com.pathik.ride.model.User.Companion.toUser
import com.pathik.ride.utils.UserPref
import com.pathik.ride.utils.Util
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDataSource @Inject constructor(
    val firestore: FirebaseFirestore,
    val storageReference: StorageReference,
) {

    suspend fun getMyTrips(userId: String): Resource<List<Any>> {
        return Resource.Success(
            firestore.collection(Constants.COLLECTION_USERS)
                .document(userId)
                .collection(Constants.COLLECTION_SHARED_LOCATIONS)
                .get().await().documents
        )
    }

//    suspend fun getWalletList(userId: String): Resource<List<Any>> {
//        return Resource.Success(
//            firestore.collection(Constants.COLLECTION_USERS)
//                .document(userId)
//                .collection(Constants.COLLECTION_WALLETS)
//                .get().await().documents
//        )
//    }


//    suspend fun getAllSavedCards(userId: String): Resource<List<Any>> {
//        return Resource.Success(
//            firestore.collection(Constants.COLLECTION_USERS)
//                .document(userId)
//                .collection(Constants.COLLECTION_SAVED_CARDS)
//                .get().await().documents
//        )
//    }


//    suspend fun saveCard(userId: String, creditCard: CreditCard): Resource.Success<Boolean> {
//        firestore.collection(Constants.COLLECTION_USERS)
//            .document(userId).collection(Constants.COLLECTION_SAVED_CARDS)
//            .add(creditCard.toMap).await()
//        return Resource.Success(true)
//    }

//    suspend fun storeWalletInformation(
//        userId: String,
//        data: HashMap<String, Any>
//    ): Resource.Success<Boolean> {
//        firestore.collection(Constants.COLLECTION_USERS)
//            .document(userId).collection(Constants.COLLECTION_WALLETS)
//            .add(data)
//        return Resource.Success(true)
//    }

    suspend fun fetchUserInfo(userId: String): Resource<User> {
        val user = firestore.collection(Constants.COLLECTION_USERS)
            .document(userId).get().await()
            .toUser()
        UserPref.putString(UserPref.KEY_NAME, user.name)
        UserPref.putString(UserPref.KEY_EMAIL, user.email)
        UserPref.putString(UserPref.KEY_USER_PROFILE_URL, user.photoUrl)
        return Resource.Success(user)
    }


    suspend fun setUserInfo(userId: String, map: HashMap<String, Any>): Resource<Boolean> {
        firestore.collection(Constants.COLLECTION_USERS)
            .document(userId)
            .set(map, SetOptions.merge())
            .await()
        return Resource.Success(true)
    }

    suspend fun setProfileWithData(
        userId: String,
        bitmap: Bitmap?,
        data: HashMap<String, Any>
    ): Resource<Boolean> {
        if (bitmap != null) {
            val profileReference = storageReference.child(Constants.PROFILES_STORAGE)
                .child("$userId.png")
            val it = profileReference.putBytes(Util.convertImage2ByteArray(bitmap)!!).await()
            val task = profileReference.downloadUrl.await()

            data["photoUrl"] = task.toString()
        }
        return setUserInfo(userId, data)
    }

}