package com.example.trainkahahai.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.trainkahahai.FcmDataProvider
import com.example.trainkahahai.SharedPrefs
import com.example.trainkahahai.base_classes.BaseActivity
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : BaseActivity() {

    private val mSharedPrefs by lazy {
        SharedPrefs(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        FirebaseMessaging.getInstance().subscribeToTopic(FcmDataProvider.topicForAll)
        startNewActivity(
            if (mSharedPrefs.isUserLoggedIn) TrainActivity() else AuthenticationActivity()
        )
        finish()
    }
}