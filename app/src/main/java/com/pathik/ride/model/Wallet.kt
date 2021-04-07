package com.pathik.ride.model

import com.google.firebase.Timestamp

enum class WalletType {
    PAY_TM,
    PHONE_PE,
    AMAZON_PAY,
}


data class Wallet(
    val id: String = "",
    val name: String = "",
    val createdAt: Timestamp? = null,
    val hasLinked: Boolean = false,
    val token: String,
    val srcId: Int? = null,
    val walletType: WalletType
)
