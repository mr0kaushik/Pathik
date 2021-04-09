package com.pathik.ride.ui.activities.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ShareCompat
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.core.view.updatePadding
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
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
import com.pathik.ride.network.Resource
import com.pathik.ride.ui.activities.payment.PaymentActivity
import com.pathik.ride.ui.activities.profile.ProfileActivity
import com.pathik.ride.ui.activities.settings.SettingsActivity
import com.pathik.ride.ui.activities.trip.TripActivity
import com.pathik.ride.utils.*
import com.pathik.ride.utils.PermissionUtil.GPS_ENABLE_REQUEST_CODE
import com.squareup.picasso.Picasso
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit

enum class DialogType {
    PERMISSION_ENABLE,
    RATIONAL_PERMISSION_ENABLE
}

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    GoogleMap.OnCameraIdleListener,
    GoogleMap.OnCameraMoveStartedListener,
    GoogleMap.OnCameraMoveCanceledListener,
    EasyPermissions.PermissionCallbacks, OnMapReadyCallback, DrawerLayout.DrawerListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var contentBinding: ContentMainBinding

    private lateinit var ivProfile: CircularImageView
    private lateinit var tvName: AppCompatTextView
    private lateinit var tvEmail: AppCompatTextView

    private lateinit var mMap: GoogleMap
    private var dragEnable: Boolean = false

    private var pinLatLng: LatLng? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var doubleBackToExitPressedOnce = false


    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = TimeUnit.SECONDS.toMillis(60)
        fastestInterval = TimeUnit.SECONDS.toMillis(30)
        maxWaitTime = TimeUnit.MINUTES.toMillis(2)
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private val viewModel by viewModels<MapViewModel>()


    @SuppressLint("VisibleForTests", "StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Util.setTransparentWindow(window)

        contentBinding = ContentMainBinding.bind(binding.root.getChildAt(0))


        binding.drawerLayout.addDrawerListener(this)


        val headerView = binding.navView.getHeaderView(0)
        ivProfile = headerView.findViewById(R.id.ivProfile)
        tvName = headerView.findViewById(R.id.tvName)
        tvEmail = headerView.findViewById(R.id.tvEmail)
        binding.navView.setNavigationItemSelectedListener(this)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationProviderClient = FusedLocationProviderClient(this)


        contentBinding.btnDrawer.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        contentBinding.btnShare.setOnClickListener {
            val text = contentBinding.tvLocationInfo.text.toString()
            if (text.isNotEmpty() && pinLatLng != null) {
                val body =
                    getString(R.string.share_body) +
                            " http://maps.google.com/maps?saddr=${pinLatLng!!.latitude},${pinLatLng!!.longitude}"
                startActivity(
                    ShareCompat.IntentBuilder(this)
                        .setType("text/plain")
                        .setText(body)
                        .setChooserTitle(R.string.share_via)
                        .intent
                )
            } else {
                contentBinding.root.snackbar(getString(R.string.unable_to_share))
            }
        }

        contentBinding.btnMyLocation.setOnClickListener {
            enableMyLocation()
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


    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap ?: return
        dragEnable = true
        contentBinding.ivLocationPicker.visible(true)
        with(mMap) {
            setOnCameraIdleListener(this@MainActivity)
            setOnCameraMoveStartedListener(this@MainActivity)
            setOnCameraMoveCanceledListener(this@MainActivity)
        }
        setMapStyle()

        with(mMap.uiSettings) {
            isMyLocationButtonEnabled = false
            isZoomGesturesEnabled = true
            isCompassEnabled = false
            isMapToolbarEnabled = false
            isZoomControlsEnabled = false
        }
        enableMyLocation()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.closeDrawers()
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

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(PermissionUtil.REQUEST_CODE_LOCATION_PERMISSION)
    private fun enableMyLocation() {
        if (PermissionUtil.hasLocationPermissions(this)) {
            mMap.isMyLocationEnabled = true
            getCurrentLocation()
        } else {
            EasyPermissions.requestPermissions(
                this,
                PermissionUtil.getLocationPermissionRequest(this)
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
        getCurrentLocation()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.permissionPermanentlyDenied(this, perms[0])) {
            showDialog(DialogType.RATIONAL_PERMISSION_ENABLE)
        } else {
            showDialog(DialogType.PERMISSION_ENABLE)
        }
    }


    private fun animateCamera(latLng: LatLng, zoomVal: Float = 15.5f) {
        val cameraPosition = CameraPosition.Builder().target(latLng).zoom(zoomVal).build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun setMapStyle(): Boolean {
        return try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (success) {
                Timber.i("Map Style parsed Successfully")
            }
            success
        } catch (e: Resources.NotFoundException) {
            Timber.e(e, "Style can't find")
            false
        }
    }


    private fun showDialog(dialogType: DialogType) {
        when (dialogType) {
            DialogType.PERMISSION_ENABLE -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.permission_required))
                    .setMessage(getString(R.string.permission_rational, "Location"))
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.ask_again)) { dialog, _ ->
                        dialog.dismiss()
                        EasyPermissions.requestPermissions(
                            this,
                            PermissionUtil.getLocationPermissionRequest(this)
                        )
                    }
                    .setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
            DialogType.RATIONAL_PERMISSION_ENABLE -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.permission_required))
                    .setMessage(
                        getString(
                            R.string.configure_permission_in_app_settings,
                            "Location"
                        )
                    )
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.open_settings)) { dialog, _ ->
                        dialog.dismiss()
                        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", packageName, null)
                        })
                    }
                    .setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }

    }

    override fun onCameraIdle() {
        if (dragEnable) {
            contentBinding.ivLocationPicker.setImageResource(R.drawable.ic_location_icon)
            setLocationAddress(mMap.cameraPosition.target)
        }
    }

    override fun onCameraMoveStarted(p: Int) {
        if (dragEnable) {
            contentBinding.tvLocationInfo.text = getString(R.string.getting_location)
            contentBinding.ivLocationPicker.setImageResource(R.drawable.ic_location_shadow)
        }
    }

    override fun onCameraMoveCanceled() {
        if (dragEnable) {
            contentBinding.ivLocationPicker.setImageResource(R.drawable.ic_location_icon)
        }
    }

    private fun getCurrentLocation() {
        viewModel.getLocationRequestDialog(locationRequest)
            .observe(this, {
                when (it) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        if (it.value.isLocationPresent) {
                            getLastLocation()
                        }
                    }
                    is Resource.Failure -> {
                        if (it.exception is ApiException) {
                            when (it.exception.statusCode) {
                                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                                    try {
                                        val resolvable = it.exception as ResolvableApiException
                                        resolvable.startResolutionForResult(
                                            this,
                                            GPS_ENABLE_REQUEST_CODE
                                        )
                                    } catch (e: IntentSender.SendIntentException) {
                                        Timber.e(e)
                                    } catch (e: ClassCastException) {
                                        Timber.e(e)
                                    }
                                }
                                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

                                }
                            }

                        }
                    }

                }
            })
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationProviderClient?.let { client ->
            viewModel.getLastLocation(client).observe(
                this
            ) {
                when (it) {
                    Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        val latLng = LatLng(it.value.latitude, it.value.longitude)
                        pinLatLng = latLng
                        animateCamera(latLng)
                        setLocationAddress(LatLng(it.value.latitude, it.value.longitude))
                    }
                    is Resource.Failure -> {
                        Timber.i("Current location not found!!")
                    }
                }
            }
        }
    }

    private fun setLocationAddress(latLng: LatLng) {
        viewModel.getAddressFromLatLng(latLng.latitude, latLng.longitude).observe(this) {
            when (it) {
                is Resource.Loading -> {
                    contentBinding.progressBar.visible(true)
                }
                is Resource.Success -> {
                    contentBinding.progressBar.visible(false)
                    if (it.value.isEmpty()) {
                        Timber.i("Location not found ${latLng.latitude},${latLng.longitude}")
//                        contentBinding.root.snackbar("Unable to get Location details")
                    } else {
                        contentBinding.tvLocationInfo.text = it.value
                    }
                }
                is Resource.Failure -> {
                    contentBinding.progressBar.visible(false)
                    Timber.e(it.exception)
                }
            }
        }
    }

    override fun onDrawerOpened(drawerView: View) {
    }

    override fun onDrawerClosed(drawerView: View) {
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        setAppearanceLight(slideOffset < 0.5)
    }

    override fun onDrawerStateChanged(newState: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.drawerLayout.removeDrawerListener(this)
    }


    private fun setAppearanceLight(set: Boolean) {
        window?.let {
            WindowCompat.getInsetsController(
                window,
                window.decorView
            )?.isAppearanceLightStatusBars = set
            WindowCompat.getInsetsController(
                window,
                window.decorView
            )?.isAppearanceLightNavigationBars = set
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return
        }

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        contentBinding.root.snackbar(getString(R.string.click_again_to_exit))
        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }
}

