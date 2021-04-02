package com.pathik.ride.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionUtil {

    var context: Context
    var activity: Activity
    var permissionResultCallback: PermissionResultCallback
    var listPermissionsNeeded = ArrayList<String>()
    var req_code = 0

    constructor(context: Context) {
        this.context = context
        activity = context as Activity
        permissionResultCallback = context as PermissionResultCallback
    }

    constructor(context: Context, permissionResultCallback: PermissionResultCallback) {
        this.context = context
        activity = context as Activity
        this.permissionResultCallback = permissionResultCallback
    }

    fun checkAndRequestPermissions(request_code: Int, vararg permissions: String): Boolean {
        listPermissionsNeeded = ArrayList()
        for (permission in permissions) {
            val hasPermission = ContextCompat.checkSelfPermission(activity, permission)
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission)
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            req_code = request_code
            ActivityCompat.requestPermissions(
                activity,
                listPermissionsNeeded.toTypedArray(),
                req_code
            )
            return false
        }
        return true
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (req_code == requestCode && grantResults.size > 0) {
            val perms: MutableMap<String, Int?> = HashMap()
            for (i in permissions.indices) {
                perms[permissions[i]] = grantResults[i]
            }
            val pending_permissions = ArrayList<String>()
            for (i in listPermissionsNeeded.indices) {
                val p = listPermissionsNeeded[i]
                if (perms[p] != null && perms[p] != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            activity,
                            listPermissionsNeeded[i]
                        )
                    ) pending_permissions.add(listPermissionsNeeded[i]) else {
                        permissionResultCallback.onNeverAskAgain(
                            requestCode,
                            listPermissionsNeeded[i]
                        )
                        return
                    }
                }
            }
            if (pending_permissions.size > 0) {
                if (listPermissionsNeeded.size == pending_permissions.size) permissionResultCallback.onPermissionDenied(
                    requestCode,
                    listPermissionsNeeded
                ) else permissionResultCallback.onPartialPermissionGranted(
                    requestCode,
                    pending_permissions
                )
            } else {
                permissionResultCallback.onPermissionGranted(requestCode)
            }
        }
    }

    interface PermissionResultCallback {
        fun onPermissionGranted(request_code: Int)
        fun onPartialPermissionGranted(request_code: Int, granted_permissions: ArrayList<String>?)
        fun onPermissionDenied(request_code: Int, listPermissionsNeeded: ArrayList<String>?)
        fun onNeverAskAgain(request_code: Int, permission: String?)
    }
}
