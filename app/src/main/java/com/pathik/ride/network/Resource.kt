package com.pathik.ride.network

sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    data class Success<out T>(val value: T) : Resource<T>()
    data class Failure(
        val exception: Exception?
    ) : Resource<Nothing>()
}
