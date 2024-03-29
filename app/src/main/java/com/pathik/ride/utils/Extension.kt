package com.pathik.ride.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.pathik.ride.R


fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.visible(visible: Boolean) {
    this.visibility = (if (visible) View.VISIBLE else View.GONE)
}

fun View.snackbar(message: String, length: Int = 3000, primaryAction: String, onTap: () -> Unit) {
    Snackbar
        .make(this, message, length)
        .also { snackbar ->
            snackbar.setAction(primaryAction) {
                snackbar.dismiss()
                onTap.invoke()
            }

                .show()
        }
}

fun View.snackbar(message: String, length: Int = 3000) {
    Snackbar
        .make(this, message, length)
        .show()
}

fun Context.getProgressDialog(titleResId: Int): MaterialAlertDialogBuilder {
    val builder = MaterialAlertDialogBuilder(this)
    val customLayout: View = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null)
    builder.setView(customLayout)
    val tvMessage: AppCompatTextView = customLayout.findViewById(R.id.tvTitle)
    tvMessage.text = this.getString(titleResId)
    tvMessage.visibility = View.VISIBLE
    builder.setCancelable(false)
    return builder
}



//fun View.addSystemWindowInsetToPadding(
//    left: Boolean = false,
//    top: Boolean = false,
//    right: Boolean = false,
//    bottom: Boolean = false
//) {
//    val (initialLeft, initialTop, initialRight, initialBottom) =
//        listOf(paddingLeft, paddingTop, paddingRight, paddingBottom)
//
//    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
//        view.updatePadding(
//            left = initialLeft + (if (left) insets.systemWindowInsetLeft else 0),
//            top = initialTop + (if (top) insets.systemWindowInsetTop else 0),
//            right = initialRight + (if (right) insets.systemWindowInsetRight else 0),
//            bottom = initialBottom + (if (bottom) insets.systemWindowInsetBottom else 0)
//        )
//
//        insets
//    }
//
////    WindowInsetsCompat.Type.ime().
//}

fun View.addSystemWindowInsetToMargin(
    left: Boolean = false,
    top: Boolean = false,
    right: Boolean = false,
    bottom: Boolean = false
) {
    val (initialLeft, initialTop, initialRight, initialBottom) =
        listOf(marginLeft, marginTop, marginRight, marginBottom)

    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        view.updateLayoutParams {
            (this as? ViewGroup.MarginLayoutParams)?.let {
                updateMargins(
                    left = initialLeft + (if (left) insets.systemWindowInsetLeft else 0),
                    top = initialTop + (if (top) insets.systemWindowInsetTop else 0),
                    right = initialRight + (if (right) insets.systemWindowInsetRight else 0),
                    bottom = initialBottom + (if (bottom) insets.systemWindowInsetBottom else 0)
                )
            }
        }

        insets
    }
}