package com.pathik.ride.model

import com.google.firebase.Timestamp

data class Trip(
    val id: String,
    val vehicleInfo: String,
    val date: Timestamp,
    val source: String,
    val destination: String,
    val amount: Double,
)
