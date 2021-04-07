package com.pathik.ride.ui.activities.trip

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.pathik.ride.R
import com.pathik.ride.databinding.ActivityTripBinding
import com.pathik.ride.databinding.AppBarLayoutBinding
import com.pathik.ride.model.Trip
import com.pathik.ride.network.Resource
import com.pathik.ride.ui.adapter.TripAdapter
import com.pathik.ride.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class TripActivity : AppCompatActivity() {


    private lateinit var binding: ActivityTripBinding
    private val viewModel by viewModels<TripViewModel>()
    private lateinit var trip: List<Trip>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appBarLayoutBinding = AppBarLayoutBinding.bind(binding.root.getChildAt(0))
        appBarLayoutBinding.tvAppBarTitle.text = getString(R.string.my_trip)
        appBarLayoutBinding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            getTripList(true)
        }
        binding.rvMyTrips.layoutManager = LinearLayoutManager(this)
        getTripList(false)
    }

    private fun getTripList(fromSwipe: Boolean) {
        viewModel.getLocalTrips().observe(this, {
            when (it) {
                is Resource.Loading -> {
                    Timber.i("Loading")
                    if (!fromSwipe) binding.swipeRefreshLayout.isRefreshing = true
                }
                is Resource.Success -> {
                    Timber.i("Success")
                    trip = it.value
                    binding.rvMyTrips.adapter = TripAdapter(trip)
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                is Resource.Failure -> {
                    Timber.e("Failure")
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.root.snackbar(
                        it.exception?.message!!,
                        Snackbar.LENGTH_INDEFINITE,
                        getString(R.string.refresh)
                    ) {
                        binding.swipeRefreshLayout.isRefreshing = true
                    }
                }
            }
        })
    }


}