package com.pathik.ride.model

import com.google.firebase.Timestamp

enum class UpiType {
    BHIM_UPI,
    PAY_TM,
    PHONE_PE,
    GOOGLE_PAY,
    AMAZON_PAY,
}

data class Upi(
    val id: String = "",
    val name: String = "",
    val createdAt: Timestamp? = null,
    val hasLinked: Boolean = false,
    val token: String,
    val srcId: Int? = null,
    val upiType: UpiType
)
