package com.pathik.ride.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot

enum class Gender(value: String) {
    MALE("Male"), FEMALE("Female")
}

data class User(
    var updatedAt: Timestamp? = null,
    val email: String? = "",
    var userId: String? = "",
    val name: String? = "",
    val phoneNumber: String? = "",
    val photoUrl: String? = "",
    var gender: Gender? = Gender.MALE,
    var createdAt: Timestamp? = null
) {


    companion object {
        fun DocumentSnapshot.toUser(): User {

            return User(
                email = getString("email"),
                userId = getString("userId"),
                name = getString("name"),
                phoneNumber = getString("phoneNumber"),
                photoUrl = getString("photoUrl"),
                gender = Gender.valueOf(getString("gender") ?: "Male")
            )

        }
    }

    val toMap = hashMapOf<String, Any?>(
        "updatedAt" to (updatedAt ?: Timestamp.now()),
        "createdAt" to (updatedAt ?: Timestamp.now()),
        "email" to email,
        "userId" to userId,
        "name" to name,
        "phoneNumber" to phoneNumber,
        "photoUrl" to "",
        "gender" to (gender?.name ?: "Male")
    )
}