package com.pathik.ride.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import timber.log.Timber

data class User(
    val createdAt: Timestamp? = null,
    val email: String = "",
    var userId: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val photoUrl: String = "",
) {


    companion object {
        fun DocumentSnapshot.toUser(): User? {
            return try {
                User(
                    email = getString("email")!!,
                    userId = getString("userId")!!,
                    name = getString("name")!!,
                    phoneNumber = getString("phoneNumber")!!,
                    photoUrl = getString("photoUrl")!!
                )
            } catch (e: Exception) {
                Timber.e(e)
                null
            }
        }
    }

    val map = hashMapOf<String, Any>(
        "createAt" to Timestamp.now(),
        "email" to email,
        "userId" to userId,
        "name" to name,
        "phoneNumber" to phoneNumber,
        "photoUrl" to "",
    )
}