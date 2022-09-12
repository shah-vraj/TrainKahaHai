package com.example.trainkahahai.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trainkahahai.R
import com.example.trainkahahai.SharedPrefs

class ViewModelFactory private constructor(
    private val mApplication: Application,
    private val mSharedPrefs: SharedPrefs
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = with(modelClass) {
        when {
            isAssignableFrom(AuthenticationViewModel::class.java) -> {
                AuthenticationViewModel(
                    mApplication,
                    mSharedPrefs
                )
            }
            isAssignableFrom(TrainViewModel::class.java) -> {
                TrainViewModel(
                    mApplication,
                    mSharedPrefs
                )
            }
            else -> {
                throw IllegalArgumentException(
                    "Unknown ViewModel class: ${modelClass.name}"
                )
            }
        }
    } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(
                    application,
                    SharedPrefs(application)
                ).also { INSTANCE = it }
            }
    }
}