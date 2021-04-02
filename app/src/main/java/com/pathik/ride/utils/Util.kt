package com.pathik.ride.utils

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pathik.ride.R

fun getSimpleDialog(
    context: Context, title: String, message: String
): MaterialAlertDialogBuilder {
    val builder = MaterialAlertDialogBuilder(context, R.style.MAlertDialogStyle)
//        builder.background = ContextCompat.getDrawable(context, R.drawable.rounded_bg_white)
    builder.setTitle(title)
        .setMessage(message)
    return builder
}