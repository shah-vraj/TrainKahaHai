package com.example.trainkahahai

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trainkahahai.viewmodel.ViewModelFactory

const val VIEW_FADE_IN_OUT_DURATION = 350L

fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProvider(this, ViewModelFactory.getInstance(application))[viewModelClass]

fun <T : ViewModel> FragmentActivity.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProvider(this, ViewModelFactory.getInstance(application))[viewModelClass]

fun View.fadeOut(animateDuration: Long = VIEW_FADE_IN_OUT_DURATION) {
    animate()
        .alpha(0f)
        .setDuration(animateDuration)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                isVisible = false
            }
        })
        .start()
}

fun View.fadeIn(animateDuration: Long = VIEW_FADE_IN_OUT_DURATION) {
    animate()
        .alpha(1f)
        .setDuration(animateDuration)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                isVisible = true
            }
        })
        .start()
}

fun LiveData<String>.isEmpty(): Boolean = this.value?.isEmpty() ?: true

fun String.validateEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun LiveData<String>.getNonNullValue() = this.value ?: ""
