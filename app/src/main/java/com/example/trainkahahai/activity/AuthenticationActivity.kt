package com.example.trainkahahai.activity

import android.os.Bundle
import android.util.Log
import com.example.trainkahahai.FcmDataProvider
import com.example.trainkahahai.R
import com.example.trainkahahai.SharedPrefs
import com.example.trainkahahai.base_classes.BaseActivity
import com.example.trainkahahai.databinding.ActivityAuthenticationBinding
import com.example.trainkahahai.obtainViewModel
import com.example.trainkahahai.viewmodel.AuthenticationViewModel
import com.google.firebase.messaging.FirebaseMessaging

class AuthenticationActivity: BaseActivity() {

    private lateinit var mBinding: ActivityAuthenticationBinding

    private val mAuthenticationViewModel by lazy {
        obtainViewModel(AuthenticationViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupAuthenticationViewModel()
    }

    private fun setupAuthenticationViewModel() {
        mBinding.authenticationViewModel = mAuthenticationViewModel
        mBinding.lifecycleOwner = this

        mAuthenticationViewModel.dismissKeyboard.observe(this) {
            dismissKeyboard()
        }
        mAuthenticationViewModel.toastMessage.observe(this) {
            toastMessage(it)
        }
        mAuthenticationViewModel.selectedAuthenticationType.observe(this) {
            val emptyString = getString(R.string.empty_string)
            mAuthenticationViewModel.showErrorMessage(emptyString)
            mAuthenticationViewModel.userName.value = emptyString
            mAuthenticationViewModel.userEmail.value = emptyString
            mAuthenticationViewModel.userPassword.value = emptyString
        }
        mAuthenticationViewModel.startTrainActivity.observe(this) {
            startNewActivity(TrainActivity())
            finish()
        }
    }
}