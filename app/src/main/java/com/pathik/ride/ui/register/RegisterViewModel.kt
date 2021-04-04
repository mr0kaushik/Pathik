package com.pathik.ride.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pathik.ride.model.User
import com.pathik.ride.network.Constants
import com.pathik.ride.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel
@Inject
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _registered: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val registered: LiveData<Resource<Boolean>>
        get() = _registered

    fun register(user: User, password: String) = viewModelScope.launch {
        _registered.value = Resource.Loading
        firebaseAuth.createUserWithEmailAndPassword(user.email, password).addOnSuccessListener {

            firestore.collection(Constants.COLLECTION_USERS)
                .document(it.user!!.uid)
                .set(user.apply { user.userId = it.user!!.uid }.map)
                .addOnSuccessListener {
                    _registered.value = Resource.Success(true)
                }
                .addOnFailureListener {
                    _registered.value = Resource.Failure(it)
                }

        }.addOnFailureListener {
            _registered.value = Resource.Failure(it)
        }
    }

}