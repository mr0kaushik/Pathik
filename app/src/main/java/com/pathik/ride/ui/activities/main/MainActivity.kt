package com.pathik.ride.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.core.view.updatePadding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.mikhaellopez.circularimageview.CircularImageView
import com.pathik.ride.R
import com.pathik.ride.databinding.ActivityMainBinding
import com.pathik.ride.databinding.ContentMainBinding
import com.pathik.ride.ui.activities.payment.PaymentActivity
import com.pathik.ride.ui.activities.profile.ProfileActivity
import com.pathik.ride.ui.activities.settings.SettingsActivity
import com.pathik.ride.ui.activities.trip.TripActivity
import com.pathik.ride.utils.PermissionUtil
import com.pathik.ride.utils.UserPref
import com.pathik.ride.utils.addSystemWindowInsetToMargin
import com.pathik.ride.utils.addSystemWindowInsetToPadding
import com.squareup.picasso.Picasso
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit


enum class DialogType {
    GPS_ENABLE,
    PERMISSION_ENABLE,
    RATIONAL_PERMISSION_ENABLE
}


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    GoogleMap.OnCameraIdleListener,
    GoogleMap.OnCameraMoveStartedListener,
    GoogleMap.OnCameraMoveCanceledListener,
    EasyPermissions.PermissionCallbacks {
    private lateinit var binding: ActivityMainBinding
    private lateinit var contentBinding: ContentMainBinding
    private lateinit var headerView: View

    private lateinit var ivProfile: CircularImageView
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView

    private lateinit var mMap: GoogleMap
    private var dragEnable: Boolean = true


    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var currentLocation: Location? = null

    private var currentLatLng: LatLng? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        transparentStatusAndNavigation()
        setTransparentStatusBarUI()
//        Util.setTransparentWindow(window)


        contentBinding = ContentMainBinding.bind(binding.root.getChildAt(0))
        headerView = binding.navView.getHeaderView(0)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    currentLatLng = LatLng(location.latitude, location.longitude)
                }
            }
        }

        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(60)
            fastestInterval = TimeUnit.SECONDS.toMillis(30)
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(callback)

        ivProfile = headerView.findViewById(R.id.ivProfile)
        tvName = headerView.findViewById(R.id.tvName)
        tvEmail = headerView.findViewById(R.id.tvEmail)

//        window.apply {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                statusBarColor = Color.TRANSPARENT
//            }
//        }

        binding.navView.setNavigationItemSelectedListener(this)


        contentBinding.btnDrawer.setOnClickListener {
            openDrawer()
        }

        contentBinding.btnShare.setOnClickListener {
            Timber.i("Btn Share")
        }

        contentBinding.btnMyLocation.setOnClickListener {
            getMyLocation()
            if (currentLatLng != null) {
                animateCamera(currentLatLng!!)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentBinding.content.setOnApplyWindowInsetsListener { view, windowInsets ->
                view.updatePadding(
                    bottom = windowInsets.getInsets(WindowInsets.Type.navigationBars()).bottom,
                    top = windowInsets.getInsets(WindowInsets.Type.statusBars()).top,
                )
                windowInsets
            }
        } else {
            contentBinding.btnMyLocation.addSystemWindowInsetToMargin(bottom = true)
            contentBinding.cardView.addSystemWindowInsetToMargin(top = true)
        }
    }

    private fun setWindowFlag(context: Context, bit: Int, b: Boolean) {
        val param = window.attributes
        if (b) {
            param.flags = param.flags or bit
        } else {
            param.flags = param.flags and (bit)
        }
        window.attributes = param
    }

    fun setTransparentStatusBarUI() {
//        supportActionBar?.hide()
//        window.apply {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                statusBarColor = Color.TRANSPARENT
//                navigationBarColor = Color.TRANSPARENT
//            }
//        }
        window?.run {
            WindowCompat.setDecorFitsSystemWindows(this, false)
        }
//        if (Build.VERSION.SDK_INT in 19..20) {
//            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
//        }
//        window.decorView.systemUiVisibility =
//            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//
//        if (Build.VERSION.SDK_INT >= 21) {
//            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
//            window.statusBarColor = Color.TRANSPARENT
//        }

    }

    override fun onResume() {
        super.onResume()
        tvName.text = UserPref.getString(UserPref.KEY_NAME)
        tvEmail.text = UserPref.getString(UserPref.KEY_EMAIL)
        if (UserPref.getString(UserPref.KEY_USER_PROFILE_URL).isNotEmpty()) {
            Picasso.get().load(UserPref.getString(UserPref.KEY_USER_PROFILE_URL))
                .placeholder(R.drawable.ic_user_place_holder)
                .error(R.drawable.ic_user_place_holder)
                .into(ivProfile)
        }
    }


    public fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }


    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.closeDrawers()

        Timber.i(item.title.toString())
        when (item.itemId) {
            R.id.menu_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
            R.id.menu_payment -> {
                startActivity(Intent(this, PaymentActivity::class.java))
            }
            R.id.menu_my_trip -> {
                startActivity(Intent(this, TripActivity::class.java))
            }
            R.id.menu_setting -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }
        return true
    }

    private fun getMyLocation() {
        if (PermissionUtil.hasLocationPermissions(this)) {
            val locationEnabled = PermissionUtil.isLocationEnabled(this)
            if (locationEnabled.first || locationEnabled.second) {
                startLocationUpdates()
            } else {
                showGPSNotEnabledDialog(DialogType.GPS_ENABLE)
            }
        } else {
            EasyPermissions.requestPermissions(
                this,
                PermissionUtil.getPermissionRequest(this)
            )
        }
    }


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationProviderClient?.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
    }

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap ?: return@OnMapReadyCallback
        with(mMap) {
            setOnCameraIdleListener(this@MainActivity)
            setOnCameraMoveStartedListener(this@MainActivity)
            setOnCameraMoveCanceledListener(this@MainActivity)
        }
        setMapStyle(R.raw.map_style)
        enableMyLocation()


        with(mMap.uiSettings) {
            isMyLocationButtonEnabled = false
            isZoomGesturesEnabled = true
        }
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(PermissionUtil.REQUEST_CODE_LOCATION_PERMISSION)
    private fun enableMyLocation() {
        if (PermissionUtil.hasLocationPermissions(this)) {
            mMap.isMyLocationEnabled = true
        } else {
            EasyPermissions.requestPermissions(
                this,
                PermissionUtil.getPermissionRequest(this)
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Timber.i("Requested Permission granted : ${perms.toString()}")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        showGPSNotEnabledDialog(DialogType.PERMISSION_ENABLE)
    }

    private fun moveCamera(latLng: LatLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    private fun animateCamera(latLng: LatLng) {
        val cameraPosition = CameraPosition.Builder().target(latLng).zoom(15.5f).build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun setMapStyle(resId: Int): Boolean {
        return try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, resId));
            if (success) {
                Timber.i("Map Style parsed Successfully");
            }
            success
        } catch (e: Resources.NotFoundException) {
            Timber.e(e, "Style can't find");
            false
        }

    }


    private fun showGPSNotEnabledDialog(dialogType: DialogType) {
        when (dialogType) {
            DialogType.GPS_ENABLE -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.enable_gps))
                    .setMessage(getString(R.string.required_for_this_app))
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.enable_now)) { dialog, _ ->
                        dialog.dismiss()
                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                    .setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
            DialogType.PERMISSION_ENABLE -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.permission_required))
                    .setMessage(getString(R.string.location_permission_rational))
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                        dialog.dismiss()
                        EasyPermissions.requestPermissions(
                            this,
                            PermissionUtil.getPermissionRequest(this)
                        )
                    }
                    .setNegativeButton(R.string.no_thanks) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }

    }

    override fun onCameraIdle() {
        if (dragEnable) {
            contentBinding.ivLocationPicker.setImageResource(R.drawable.ic_marker)
        }
    }

    override fun onCameraMoveStarted(p: Int) {
        if (dragEnable) {
            contentBinding.ivLocationPicker.setImageResource(R.drawable.ic_shadow_marker)
        }
    }

    override fun onCameraMoveCanceled() {
        if (dragEnable) {
            contentBinding.ivLocationPicker.setImageResource(R.drawable.ic_marker)
        }
        Timber.i("onCameraMoveCanceled")

    }


    /*
    private fun openLocationSetting() {

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val task: Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient(requireContext()).checkLocationSettings(builder.build())
        task.addOnCompleteListener { task1 ->
            try {
                val response: LocationSettingsResponse = task1.getResult(ApiException::class.java)
                Timber.i(response.toString())
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                         // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            val resolvable = exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(activity, GPS_ENABLE_REQUEST_CODE)
                        } catch (e: SendIntentException) {
                            e.printStackTrace()
                        } catch (e: ClassCastException) {
                            e.printStackTrace()
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val states = LocationSettingsStates.fromIntent(data)
        when (requestCode) {
            GPS_ENABLE_REQUEST_CODE -> when (resultCode) {
                Activity.RESULT_OK -> getDeviceLocation()
                Activity.RESULT_CANCELED -> showDialog(GPS_ENABLE_TYPE)
                else -> {
                }
            }
        }
    }
    */
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.forEach { fragment ->
//            fragment.onActivityResult(requestCode, resultCode, data)
//        }
//    }


}
