package com.pathik.ride.ui.activities.main

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.android.gms.location.*
import com.pathik.ride.PathikApp
import com.pathik.ride.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.util.*

class MapViewModel : ViewModel() {
    fun getLocationRequestDialog(locationRequest: LocationRequest) = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        try {
            val builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(locationRequest)
            val response: LocationSettingsResponse =
                LocationServices.getSettingsClient(PathikApp.context())
                    .checkLocationSettings(builder.build()).await()
            emit(Resource.Success(response.locationSettingsStates))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation(fusedLocationProviderClient: FusedLocationProviderClient) =
        liveData(Dispatchers.IO) {
            emit(Resource.Loading)
            try {
                val location = fusedLocationProviderClient.lastLocation.await()
                emit(Resource.Success(location))
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }

    fun getAddressFromLatLng(lat: Double, lng: Double) = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        val gcd = Geocoder(PathikApp.context(), Locale.getDefault())
        val addresses: List<Address>
        try {
            addresses = gcd.getFromLocation(lat, lng, 1)
            if (addresses.isNotEmpty()) {
                emit(Resource.Success(addresses[0].getAddressLine(0)))
            } else {
                emit(Resource.Success(""))
            }
        } catch (e: IOException) {
            emit(Resource.Failure(e))
        }
    }
}