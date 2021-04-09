package com.pathik.ride.ui.activities.trip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import com.pathik.ride.model.DataSource
import com.pathik.ride.network.FirebaseDataSource
import com.pathik.ride.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class TripViewModel
@Inject
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirebaseDataSource: FirebaseDataSource
) : ViewModel() {


//    fun getMyTrips(userId: String) =
//        liveData(Dispatchers.IO) {
//            emit(Resource.Loading)
//            try {
//                val trips = firebaseFirebaseDataSource.getMyTrips(firebaseAuth.currentUser?.uid!!)
//                emit(trips)
//            } catch (e: Exception) {
//                emit(Resource.Failure(e))
//            }
//        }

    fun getLocalTrips() = liveData(Dispatchers.IO) {
        emit(Resource.Loading)

        try {
            Thread.sleep(2000)
            val mutableList = DataSource.listOfTrips
            emit(Resource.Success(mutableList))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }
}