package com.skillbox.core.snackbar

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.skillbox.core.R
import com.skillbox.core.extensions.getDimension
import kotlinx.android.synthetic.main.item_custom_snackbar.view.*

class CustomSnackbar(
        parent: ViewGroup,
        content: CustomSnackbarView
) : BaseTransientBottomBar<CustomSnackbar>(parent, content, content) {

    init {
        getView().setBackgroundColor(
                ContextCompat.getColor(
                        view.context,
                        android.R.color.transparent
                )
        )
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.TOP
        params.topMargin = view.context.getDimension(100F)
        getView().layoutParams = params
    }

    companion object {

        fun make(viewGroup: ViewGroup, isCache: Boolean, text: String): CustomSnackbar {
            val customView = LayoutInflater.from(viewGroup.context).inflate(
                    R.layout.layout_custom_snackbar,
                    viewGroup,
                    false
            ) as CustomSnackbarView

            if(isCache) {
                customView.tvMessage.setTextColor(ContextCompat.getColor(customView.context, R.color.colorCache))
                customView.ivState.setBackgroundDrawable(customView.context.getDrawable(R.drawable.ic_notification))
            }
            else {
                customView.tvMessage.setTextColor(ContextCompat.getColor(customView.context, R.color.colorError))
                customView.ivState.setBackgroundDrawable(customView.context.getDrawable(R.drawable.ic_error))
            }

            customView.ivClose.setOnClickListener {

            }

            customView.tvMessage.text = text

            return CustomSnackbar(viewGroup, customView)
        }

    }

}