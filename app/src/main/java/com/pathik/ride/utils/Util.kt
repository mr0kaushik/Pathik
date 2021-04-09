package com.pathik.ride.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.OpenableColumns
import android.view.Window
import androidx.core.content.FileProvider
import androidx.core.view.WindowCompat
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.pathik.ride.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


object Util {

    fun setTransparentWindow(window: Window?) {
        window?.run {
            WindowCompat.setDecorFitsSystemWindows(this, false)
        }
    }

    fun convertImage2ByteArray(bitmap: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
        return stream.toByteArray()
    }

//    @Throws(IOException::class)
//    fun getBytes(inputStream: InputStream): ByteArray? {
//        val byteBuffer = ByteArrayOutputStream()
//        val bufferSize = 1024
//        val buffer = ByteArray(bufferSize)
//        var len = 0
//        while (inputStream.read(buffer).also { len = it } != -1) {
//            byteBuffer.write(buffer, 0, len)
//        }
//        return byteBuffer.toByteArray()
//    }
//
//    fun convertByteArrayToBitmap(array: ByteArray): Bitmap? {
//        return BitmapFactory.decodeByteArray(array, 0, array.size)
//    }

//    fun getRealPathFromURI(contentURI: Uri, context: Context): String? {
//        val result: String?
//        val cursor = context.contentResolver.query(contentURI, null, null, null, null)
//        if (cursor == null) { // Source is Dropbox or other similar local file path
//            result = contentURI.path
//        } else {
//            cursor.moveToFirst()
//            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
//            result = cursor.getString(idx)
//            cursor.close()
//        }
//        return result
//    }

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
                R.string.an_error_occur
            }
            is FirebaseTooManyRequestsException -> {
                R.string.an_error_occur
            }
            else -> {
                R.string.an_error_occur
            }
        }

    }

    fun currencyFormat(amount: Double): String {
        val formatter = DecimalFormat("###,###,##0.00")
        return "\u20B9 ${formatter.format(amount)}"
    }

    @SuppressLint("SimpleDateFormat")
    fun prettyDateFormatter(timestamp: Timestamp): String {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val parsedDate =
                LocalDateTime.ofInstant(timestamp.toDate().toInstant(), ZoneId.systemDefault())
            parsedDate.format(DateTimeFormatter.ofPattern("EEE, MMM d, yyyy, h:mm a"))
        } else {
            val formatter = SimpleDateFormat("EEE, MMM d, yyyy, h:mm a")
            formatter.format(timestamp.toDate())
        }
    }

    fun getImageDestinationPath(userId: String, context: Context): Uri {
        val root =
            File(
                context.externalCacheDir,
                "profile"
            )
        root.mkdirs()
        val sdImageMainDirectory = File(root, "profile_$userId.jpg")
        return FileProvider.getUriForFile(
            context,
            context.packageName + ".provider",
            sdImageMainDirectory
        )
    }


    @SuppressLint("Recycle")
    fun queryName(contentResolver: ContentResolver?, sourceUri: Uri): String {
        val returnCursor: Cursor = contentResolver?.query(sourceUri, null, null, null, null)!!
        val nameIndex: Int = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name: String = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
    }

}
