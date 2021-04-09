package com.pathik.ride.utils

import android.Manifest
import android.content.Context
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import com.pathik.ride.R
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.models.PermissionRequest

object PermissionUtil {

    const val REQUEST_CODE_LOCATION_PERMISSION = 100
    const val GPS_ENABLE_REQUEST_CODE = 101
    const val REQUEST_CAMERA_CODE = 102
    const val READ_EXTERNAL_STORAGE_CODE = 103

    fun getLocationPermissionRequest(context: Context) = PermissionRequest.Builder(context)
        .code(REQUEST_CODE_LOCATION_PERMISSION)
        .perms(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        .theme(R.style.MAlertDialogStyle)
        .rationale(R.string.location_permission_rational)
        .build()

    fun getCameraPermissionRequest(context: Context) = PermissionRequest.Builder(context)
        .code(REQUEST_CAMERA_CODE)
        .perms(arrayOf(Manifest.permission.CAMERA))
        .theme(R.style.MAlertDialogStyle)
        .rationale(context.getString(R.string.permission_rational, "Camera"))
        .build()

    fun isLocationEnabled(context: Context): Pair<Boolean, Boolean> {
        val lm =
            context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        return Pair<Boolean, Boolean>(
            lm.isProviderEnabled(LocationManager.GPS_PROVIDER),
            lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        )
    }


    fun hasLocationPermissions(context: Context): Boolean {
        return EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    }

}
