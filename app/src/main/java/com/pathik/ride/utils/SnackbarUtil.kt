package com.pathik.ride.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.pathik.ride.R


class SnackbarUtil(private val context: Context, private val snackbar: Snackbar) {

    companion object {
        val ERROR_COLOR = Color.parseColor("#FFF15249")
        val SUCCESS_COLOR = Color.parseColor("#FF32BA7C")
        val WARNING_COLOR = Color.parseColor("#FFFDBF00")
        val SNACK_BAR_BACKGROUND_COLOR = Color.parseColor("#FFFFFFFF")
    }

    private val bgLayout: RelativeLayout?
    private val sideView: View?
    private val ivIcon: AppCompatImageView
    private val tvTitle: AppCompatTextView?
    private val tvSubtitle: AppCompatTextView?
    var snackView: View

    fun getSuccessSnackBarView(title: String?, subtitle: String?): Snackbar {
        titleText(title)
        subtitleText(subtitle)
        setIcon(ContextCompat.getDrawable(context, R.drawable.ic_success))
        setSideViewColor(SUCCESS_COLOR)
        setBackgroundColor(SNACK_BAR_BACKGROUND_COLOR)
        return snackbar
    }

    fun getErrorSnackBarView(title: String?, subtitle: String?): Snackbar {
        titleText(title)
        subtitleText(subtitle)
        setIcon(ContextCompat.getDrawable(context, R.drawable.ic_error))
        setSideViewColor(ERROR_COLOR)
        setBackgroundColor(SNACK_BAR_BACKGROUND_COLOR)
        return snackbar
    }

    fun getWarningSnackBarView(title: String?, subtitle: String?): Snackbar {
        titleText(title)
        subtitleText(subtitle)
        setIcon(ContextCompat.getDrawable(context, R.drawable.ic_info))
        setSideViewColor(WARNING_COLOR)
        setBackgroundColor(SNACK_BAR_BACKGROUND_COLOR)
        return snackbar
    }

    fun titleText(text: String?) {
        if (tvTitle != null) {
            if (text != null) {
                tvTitle.text = text
                tvTitle.visibility = View.VISIBLE
            } else {
                tvTitle.visibility = View.GONE
            }
        }
    }

    fun subtitleText(text: String?) {
        if (tvSubtitle != null) {
            if (text != null) {
                tvSubtitle.text = text
                tvSubtitle.visibility = View.VISIBLE
            } else {
                tvSubtitle.visibility = View.GONE
            }
        }
    }

    fun setIcon(drawable: Drawable?) {
        if (tvTitle != null) {
            ivIcon.setImageDrawable(drawable)
        }
    }

    fun setSideViewColor(color: Int) {
        sideView?.setBackgroundColor(color)
    }

    fun setBackgroundColor(color: Int) {
        bgLayout?.setBackgroundColor(color)
    }

    init {
        snackView = LayoutInflater.from(context).inflate(R.layout.snack_bar_layout, null)
        bgLayout = snackView.findViewById(R.id.bgLayout)
        ivIcon = snackView.findViewById(R.id.ivIcon)
        sideView = snackView.findViewById(R.id.sideView)
        tvTitle = snackView.findViewById(R.id.tvTitle)
        tvSubtitle = snackView.findViewById(R.id.tvSubtitle)
        val layout = snackbar.view as SnackbarLayout
        val textView = layout.findViewById<TextView>(R.id.snackbar_text)
        textView.visibility = View.INVISIBLE
        layout.setPadding(0, 0, 0, 0)
        layout.addView(snackView, 0)
    }
}
