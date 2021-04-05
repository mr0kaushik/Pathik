package com.pathik.ride.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.pathik.ride.R

fun View.snackbar(message: String, primaryAction: String, onTap: () -> Unit) {
    Snackbar
        .make(this, message, Snackbar.LENGTH_INDEFINITE)
        .also { snackbar ->
            snackbar.setAction(primaryAction) {
                snackbar.dismiss()
                onTap()
            }
                .show()
        }
}

fun View.snackbar(message: String, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar
        .make(this, message, length)
        .show()
}

fun Context.getProgressDialog(titleResId: Int): MaterialAlertDialogBuilder {
    val builder = MaterialAlertDialogBuilder(this, R.style.MAlertDialogStyle)
    val customLayout: View = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null)
    builder.setView(customLayout)
    val tvMessage: AppCompatTextView = customLayout.findViewById(R.id.tvTitle)
    tvMessage.text = this.getString(titleResId)
    tvMessage.visibility = View.VISIBLE
    builder.setCancelable(false)
    return builder
}
