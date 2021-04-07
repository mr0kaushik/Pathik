package com.mindorks.ridesharing.utils

import android.content.Context
import android.graphics.*
import android.location.LocationManager
import com.google.android.gms.maps.model.LatLng
import com.pathik.ride.R
import timber.log.Timber
import kotlin.math.abs
import kotlin.math.atan


object MapUtils {

    fun getCarBitmap(context: Context): Bitmap {
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_marker)
        return Bitmap.createScaledBitmap(bitmap, 50, 100, false)
    }

    fun getDestinationBitmap(): Bitmap {
        val height = 20
        val width = 20
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), paint)
        return bitmap
    }

    fun getRotation(start: LatLng, end: LatLng): Float {
        val latDifference: Double = abs(start.latitude - end.latitude)
        val lngDifference: Double = abs(start.longitude - end.longitude)
        var rotation = -1F
        when {
            start.latitude < end.latitude && start.longitude < end.longitude -> {
                rotation = Math.toDegrees(atan(lngDifference / latDifference)).toFloat()
            }
            start.latitude >= end.latitude && start.longitude < end.longitude -> {
                rotation = (90 - Math.toDegrees(atan(lngDifference / latDifference)) + 90).toFloat()
            }
            start.latitude >= end.latitude && start.longitude >= end.longitude -> {
                rotation = (Math.toDegrees(atan(lngDifference / latDifference)) + 180).toFloat()
            }
            start.latitude < end.latitude && start.longitude >= end.longitude -> {
                rotation =
                    (90 - Math.toDegrees(atan(lngDifference / latDifference)) + 270).toFloat()
            }
        }
        Timber.i("Get marker rotation $rotation")
        return rotation
    }

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun showGPSNotEnabledDialog(context: Context) {
//        AlertDialog.Builder(context)
//            .setTitle(context.getString(R.string.enable_gps))
//            .setMessage(context.getString(R.string.required_for_this_app))
//            .setCancelable(false)
//            .setPositiveButton(context.getString(R.string.enable_now)) { _, _ ->
//                context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
//            }
//            .show()
    }


    const val SUCCESS_RESULT = 0

    const val FAILURE_RESULT = 1

    const val PACKAGE_NAME = "com.pathik.ride"

    const val RECEIVER = "$PACKAGE_NAME.RECEIVER"

    const val RESULT_DATA_KEY = "$PACKAGE_NAME.RESULT_DATA_KEY"

    const val LOCATION_DATA_EXTRA = "$PACKAGE_NAME.LOCATION_DATA_EXTRA"

    const val LOCATION_DATA_AREA = "$PACKAGE_NAME.LOCATION_DATA_AREA"
    const val LOCATION_DATA_CITY = "$PACKAGE_NAME.LOCATION_DATA_CITY"
    const val LOCATION_DATA_STREET = "$PACKAGE_NAME.LOCATION_DATA_STREET"

}