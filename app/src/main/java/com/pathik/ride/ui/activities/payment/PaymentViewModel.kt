package com.pathik.ride.ui.activities.payment

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
class PaymentViewModel
@Inject
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirebaseDataSource: FirebaseDataSource
) : ViewModel() {


    fun getWalletInformation(userId: String) =
        liveData(Dispatchers.IO) {
            emit(Resource.Loading)
            try {
                val trips = firebaseFirebaseDataSource.getMyTrips(firebaseAuth.currentUser?.uid!!)
                emit(trips)
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }

    fun getLocalPayments() = liveData(Dispatchers.IO) {
        emit(Resource.Loading)

        try {
            Thread.sleep(2000)
            val upis = DataSource.listOfUpi
            val wallets = DataSource.listOfWallet
            emit(Resource.Success(Pair(upis, wallets)))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }


}