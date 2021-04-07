package com.pathik.ride.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot

data class CreditCard(
    val name: String = "",
    val number: String = "",
    val expiryMonth: Int = 0,
    var expiryYear: Int = 2020,
    var createdAt: Timestamp? = null,
) {


    companion object {
        fun DocumentSnapshot.toCreditCard(): CreditCard {
            return CreditCard(
                name = this.getString("name")!!,
                number = this.getString("number")!!,
                expiryMonth = this.get("expiryMonth") as Int,
                expiryYear = this.get("expiryYear") as Int,
                createdAt = this.getTimestamp("createdAt")
            )

        }
    }

    val toMap = hashMapOf<String, Any?>(
        "name" to name,
        "number" to number,
        "expiryMonth" to expiryMonth,
        "expiryYear" to expiryYear,
        "createdAt" to createdAt,
    )
}