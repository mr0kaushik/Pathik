package com.pathik.ride.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.view.Window
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.pathik.ride.R
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt

object Util {
    fun setTransparentWindow(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            window.setDecorFitsSystemWindows(false)
        } else {
            window.statusBarColor = Color.TRANSPARENT
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val winParams = window.attributes
//            winParams.flags =
//                winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
//            window.attributes = winParams
//            window.decorView.systemUiVisibility =
//                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        }
    }

    fun dpToPx(dp: Float): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (dp * density).roundToInt()
    }


    fun getSimpleDialog(
        context: Context, title: String, message: String
    ): MaterialAlertDialogBuilder {
        val builder = MaterialAlertDialogBuilder(context, R.style.MAlertDialogStyle)
        builder.setTitle(title)
            .setMessage(message)
        return builder
    }


    fun convertImage2ByteArray(bitmap: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
        return stream.toByteArray()
    }

    fun convertByteArray2Image(array: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(array, 0, array.size)
    }

    fun getRealPathFromURI(contentURI: Uri, context: Context): String? {
        val result: String?
        val cursor = context.contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    fun getSimpleErrorResourceId(e: FirebaseException): Int {

        return when (e) {
            is FirebaseAuthException -> {
                when (e.errorCode) {
                    "ERROR_INVALID_CREDENTIAL" -> {
                        R.string.error_login_credential_malformed_or_expired
                    }
                    "ERROR_INVALID_EMAIL" -> {
                        R.string.error_login_invalid_email
                    }
                    "ERROR_WRONG_PASSWORD" -> {
                        R.string.error_login_credential_malformed_or_expired
                    }
                    "ERROR_EMAIL_ALREADY_IN_USE" -> {
                        R.string.error_login_email_already_in_use
                    }
                    "ERROR_USER_DISABLED" -> {
                        R.string.error_login_user_disabled
                    }
                    "ERROR_USER_TOKEN_EXPIRED" -> {
                        R.string.error_login_user_token_expired
                    }
                    "ERROR_USER_NOT_FOUND" -> {
                        R.string.error_login_user_not_found
                    }
                    "ERROR_INVALID_USER_TOKEN" -> {
                        R.string.error_login_invalid_user_token
                    }
                    "ERROR_OPERATION_NOT_ALLOWED" -> {
                        R.string.error_login_operation_not_allowed
                    }
                    "ERROR_WEAK_PASSWORD" -> {
                        R.string.error_login_password_is_weak
                    }
                    else -> {
                        R.string.common_auth_error
                    }
                }
            }
            is FirebaseNetworkException -> {
                R.string.error_network_exception
            }
            is FirebaseFirestoreException -> {
                R.string.please_try_again_later
            }
            is FirebaseTooManyRequestsException -> {
                R.string.please_try_again_later
            }
            else -> {
                R.string.an_error_occur

            }
        }

    }

}
