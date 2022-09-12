/*
 * Copyright (C) Shield AI, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.example.trainkahahai.binding_adapter

import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.example.trainkahahai.R
import com.example.trainkahahai.fadeIn
import com.example.trainkahahai.fadeOut

object ViewBindingAdapter {

    @BindingAdapter("isVisibleGone")
    @JvmStatic
    fun isVisibleGone(view: View, isVisibleGone: Boolean) {
        view.visibility = if (isVisibleGone) View.VISIBLE else View.GONE
    }

    @BindingAdapter("isVisibleGoneFadeInOutAnimated")
    @JvmStatic
    fun View.isVisibleGoneFadeInOutAnimated(isVisibleGone: Boolean) {
        if (isVisibleGone) fadeIn() else fadeOut()
    }

    @BindingAdapter("isVisibleInvisible")
    @JvmStatic
    fun isVisibleInvisible(view: View, isVisibleInvisible: Boolean) {
        view.visibility = if (isVisibleInvisible) View.VISIBLE else View.INVISIBLE
    }

    @BindingAdapter("setIcon")
    @JvmStatic
    fun ImageView.setImageViewSrcFromId(@DrawableRes resourceId: Int?) {
        resourceId ?: run {
            setImageDrawable(null)
            return
        }
        if (resourceId <= 0) {
            setImageDrawable(null)
            return
        }
        setImageResource(resourceId)
    }

    @BindingAdapter("disableButton", "enabledButtonText", requireAll = true)
    @JvmStatic
    fun Button.disableButton(isDisabled: Boolean, enabledButtonText: String) {
        if (isDisabled) {
            text = context.getString(R.string.empty_string)
            alpha = 0.5f
            isClickable = false
        } else {
            text = enabledButtonText
            alpha = 1f
            isClickable = true
        }
    }
}