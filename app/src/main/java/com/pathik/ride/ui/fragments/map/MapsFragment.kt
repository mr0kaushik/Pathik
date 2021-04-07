//package com.pathik.ride.ui.fragments.map
//
//import android.annotation.SuppressLint
//import android.content.Intent
//import android.content.res.Resources
//import android.location.Location
//import android.os.Bundle
//import android.os.Looper
//import android.provider.Settings
//import android.view.View
//import androidx.fragment.app.Fragment
//import com.google.android.gms.location.*
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.CameraPosition
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MapStyleOptions
//import com.google.android.material.dialog.MaterialAlertDialogBuilder
//import com.pathik.ride.R
//import com.pathik.ride.databinding.FragmentMapsBinding
//import com.pathik.ride.ui.activities.MainActivity
//import com.pathik.ride.utils.PermissionUtil
//import com.pathik.ride.utils.PermissionUtil.REQUEST_CODE_LOCATION_PERMISSION
//import com.vmadalin.easypermissions.EasyPermissions
//import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
//import dagger.hilt.android.AndroidEntryPoint
//import timber.log.Timber
//import java.util.concurrent.TimeUnit
//
//enum class DialogType {
//    GPS_ENABLE,
//    PERMISSION_ENABLE
//}
//
//@AndroidEntryPoint
//class MapsFragment : Fragment(R.layout.fragment_maps),
//    GoogleMap.OnCameraIdleListener,
//    GoogleMap.OnCameraMoveListener,
//    GoogleMap.OnCameraMoveStartedListener,
//    GoogleMap.OnCameraMoveCanceledListener, EasyPermissions.PermissionCallbacks
//{
//
//    private lateinit var binding: FragmentMapsBinding
//    private lateinit var map: GoogleMap
//
//    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
//    private lateinit var locationCallback: LocationCallback
//    private lateinit var locationRequest: LocationRequest
//    private var currentLocation: Location? = null
//
//    private var currentLatLng: LatLng? = null
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding = FragmentMapsBinding.bind(view)
//
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult?) {
//                locationResult ?: return
//                for (location in locationResult.locations) {
//                    currentLatLng = LatLng(location.latitude, location.longitude)
//                }
//            }
//        }
//
//        locationRequest = LocationRequest.create().apply {
//            interval = TimeUnit.SECONDS.toMillis(60)
//            fastestInterval = TimeUnit.SECONDS.toMillis(30)
//            maxWaitTime = TimeUnit.MINUTES.toMillis(2)
//            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        }
//
//        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
//        mapFragment?.getMapAsync(callback)
//
//        binding.btnDrawer.setOnClickListener {
//            (activity as MainActivity).openDrawer()
//        }
//
//        binding.btnMyLocation.setOnClickListener {
//            getMyLocation()
//            if (currentLatLng != null) {
//                animateCamera(currentLatLng!!)
//            }
//        }
//    }
//
//    private fun getMyLocation() {
//        if (PermissionUtil.hasLocationPermissions(requireContext())) {
//            val locationEnabled = PermissionUtil.isLocationEnabled(requireContext())
//            if (locationEnabled.first || locationEnabled.second) {
//                startLocationUpdates()
//            } else {
//                showGPSNotEnabledDialog(DialogType.GPS_ENABLE)
//            }
//        } else {
//            EasyPermissions.requestPermissions(
//                this,
//                PermissionUtil.getPermissionRequest(requireContext())
//            )
//        }
//    }
//
//
//    @SuppressLint("MissingPermission")
//    private fun startLocationUpdates() {
//        fusedLocationProviderClient?.requestLocationUpdates(
//            locationRequest,
//            locationCallback,
//            Looper.getMainLooper()
//        )
//    }
//
//    override fun onPause() {
//        super.onPause()
//        stopLocationUpdates()
//    }
//
//    private fun stopLocationUpdates() {
//        fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
//    }
//
//    private val callback = OnMapReadyCallback { googleMap ->
//        map = googleMap ?: return@OnMapReadyCallback
//        with(map){
//            setOnCameraIdleListener(this@MapsFragment)
//            setOnCameraMoveStartedListener(this@MapsFragment)
//            setOnCameraMoveListener(this@MapsFragment)
//            setOnCameraMoveCanceledListener(this@MapsFragment)
//        }
//        setMapStyle(R.raw.map_style)
//        enableMyLocation()
//
//
//        with(map.uiSettings) {
//            isMyLocationButtonEnabled = false
//            isZoomGesturesEnabled = true
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    @AfterPermissionGranted(REQUEST_CODE_LOCATION_PERMISSION)
//    private fun enableMyLocation() {
//        if (PermissionUtil.hasLocationPermissions(requireContext())) {
//            map.isMyLocationEnabled = true
//        } else {
//            EasyPermissions.requestPermissions(
//                this,
//                PermissionUtil.getPermissionRequest(requireContext())
//            )
//        }
//    }
//
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
//    }
//
//    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
//        Timber.i("Requested Permission granted : ${perms.toString()}")
//    }
//
//    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
//        showGPSNotEnabledDialog(DialogType.PERMISSION_ENABLE)
//    }
//
//    private fun moveCamera(latLng: LatLng) {
//        map.moveCamera(CameraUpdateFactory.newLatLng(latLng))
//    }
//
//    private fun animateCamera(latLng: LatLng) {
//        val cameraPosition = CameraPosition.Builder().target(latLng).zoom(15.5f).build()
//        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
//    }
//
//    private fun setMapStyle(resId: Int): Boolean {
//        return try {
//            val success =
//                map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), resId));
//            if (success) {
//                Timber.i("Map Style parsed Successfully");
//            }
//            success
//        } catch (e: Resources.NotFoundException) {
//            Timber.e(e, "Style can't find");
//            false
//        }
//
//    }
//
//    override fun onResume() {
//        super.onResume()
//        Timber.i("on resume")
//    }
//
//    override fun onStart() {
//        super.onStart()
//        Timber.i("On Start")
//    }
//
//    override fun onStop() {
//        super.onStop()
//        Timber.i("ON Stop")
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Timber.i("Ondestroy")
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        Timber.i("Ondestroyview")
//    }
//
//
//    private fun showGPSNotEnabledDialog(dialogType: DialogType) {
//        when (dialogType) {
//            DialogType.GPS_ENABLE -> {
//                MaterialAlertDialogBuilder(requireContext())
//                    .setTitle(getString(R.string.enable_gps))
//                    .setMessage(getString(R.string.required_for_this_app))
//                    .setCancelable(true)
//                    .setPositiveButton(getString(R.string.enable_now)) { dialog, _ ->
//                        dialog.dismiss()
//                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
//                    }
//                    .setNegativeButton(R.string.cancel) { dialog, _ ->
//                        dialog.dismiss()
//                    }
//                    .show()
//            }
//            DialogType.PERMISSION_ENABLE -> {
//                MaterialAlertDialogBuilder(requireContext())
//                    .setTitle(getString(R.string.permission_required))
//                    .setMessage(getString(R.string.location_permission_rational))
//                    .setCancelable(true)
//                    .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
//                        dialog.dismiss()
//                        EasyPermissions.requestPermissions(
//                            this,
//                            PermissionUtil.getPermissionRequest(requireContext())
//                        )
//                    }
//                    .setNegativeButton(R.string.no_thanks) { dialog, _ ->
//                        dialog.dismiss()
//                    }
//                    .show()
//            }
//        }
//
//    }
//
//    override fun onCameraIdle() {
//        Timber.i("onCameraIdle")
//    }
//
//    override fun onCameraMove() {
//        Timber.i("onCameraMove")
//
//    }
//
//    override fun onCameraMoveStarted(p: Int) {
//        Timber.i("onCameraMoveStarted Started : $p")
//    }
//
//    override fun onCameraMoveCanceled() {
//        Timber.i("onCameraMoveCanceled")
//
//    }
//
//
//    /*
//    private fun openLocationSetting() {
//
//        val builder = LocationSettingsRequest.Builder()
//        builder.addLocationRequest(locationRequest)
//        val task: Task<LocationSettingsResponse> =
//            LocationServices.getSettingsClient(requireContext()).checkLocationSettings(builder.build())
//        task.addOnCompleteListener { task1 ->
//            try {
//                val response: LocationSettingsResponse = task1.getResult(ApiException::class.java)
//                Timber.i(response.toString())
//            } catch (exception: ApiException) {
//                when (exception.statusCode) {
//                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                         // Location settings are not satisfied. But could be fixed by showing the
//                        // user a dialog.
//                        try {
//                            // Cast to a resolvable exception.
//                            val resolvable = exception as ResolvableApiException
//                            // Show the dialog by calling startResolutionForResult(),
//                            // and check the result in onActivityResult().
//                            resolvable.startResolutionForResult(activity, GPS_ENABLE_REQUEST_CODE)
//                        } catch (e: SendIntentException) {
//                            e.printStackTrace()
//                        } catch (e: ClassCastException) {
//                            e.printStackTrace()
//                        }
//                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
//                    }
//                }
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        val states = LocationSettingsStates.fromIntent(data)
//        when (requestCode) {
//            GPS_ENABLE_REQUEST_CODE -> when (resultCode) {
//                Activity.RESULT_OK -> getDeviceLocation()
//                Activity.RESULT_CANCELED -> showDialog(GPS_ENABLE_TYPE)
//                else -> {
//                }
//            }
//        }
//    }
//    */
//}
//
