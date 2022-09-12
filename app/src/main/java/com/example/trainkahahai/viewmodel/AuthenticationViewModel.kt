package com.example.trainkahahai.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.trainkahahai.FcmDataProvider
import com.example.trainkahahai.R
import com.example.trainkahahai.SharedPrefs
import com.example.trainkahahai.base_classes.BaseViewModel
import com.example.trainkahahai.isEmpty
import com.example.trainkahahai.model.UserDataModel
import com.example.trainkahahai.validateEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthenticationViewModel(
    private val mAppContext: Application,
    private val mSharedPrefs: SharedPrefs
): BaseViewModel() {

    private val mDismissKeyboard = MutableLiveData<Boolean>()
    val dismissKeyboard: LiveData<Boolean> get() = mDismissKeyboard

    private val mShowErrorMessage = MutableLiveData<String>()
    val showErrorMessage: LiveData<String> get() = mShowErrorMessage

    private val mShowProgressBar = MutableLiveData(false)
    val showProgressBar: LiveData<Boolean> get() = mShowProgressBar

    private val mStartTrainActivity = MutableLiveData<Boolean>()
    val startTrainActivity: LiveData<Boolean> get() = mStartTrainActivity

    private val mSelectedAuthenticationType =
        MutableLiveData(mAppContext.getString(R.string.signup))
    val selectedAuthenticationType: LiveData<String> get() = mSelectedAuthenticationType

    private val mAuthenticationSegmentList = MutableLiveData(
        listOf(mAppContext.getString(R.string.signup), mAppContext.getString(R.string.login))
    )
    val authenticationSegmentList: LiveData<List<String>> get() = mAuthenticationSegmentList

    val isNewUser: LiveData<Boolean> get() = Transformations.map(mSelectedAuthenticationType) {
        when (it) {
            mAppContext.getString(R.string.signup) -> true
            mAppContext.getString(R.string.login) -> false
            else -> true
        }
    }

    val userName = MutableLiveData<String>()
    val userEmail = MutableLiveData<String>()
    val userPassword = MutableLiveData<String>()

    private val mUserName: String
        get() = userName.value ?: ""
    private val mUserEmail: String
        get() = userEmail.value ?: ""
    private val mUserPassword: String
        get() = userPassword.value ?: ""

    fun dismissKeyboard() {
        mDismissKeyboard.value = true
    }

    fun onAuthenticationSegmentSelected(selectedSegment: String) {
        mSelectedAuthenticationType.value = selectedSegment
    }

    fun authenticateUser() {
        if (areValidCredentials()) {
            mShowProgressBar.value = true

            when (mSelectedAuthenticationType.value) {
                mAppContext.getString(R.string.signup) -> {
                    signUpUser()
                }
                mAppContext.getString(R.string.login) -> {
                    loginUser()
                }
            }
        }
    }

    private fun areValidCredentials(): Boolean =
        if (selectedAuthenticationType.value == mAppContext.getString(R.string.signup) &&
            userName.isEmpty()
        ) {
            showErrorMessage(mAppContext.getString(R.string.empty_username))
            false
        } else if (userEmail.isEmpty()) {
            showErrorMessage(mAppContext.getString(R.string.empty_email))
            false
        } else if (userEmail.value?.validateEmail() == false) {
            showErrorMessage(mAppContext.getString(R.string.wrong_email))
            false
        } else if (userPassword.isEmpty()) {
            showErrorMessage(mAppContext.getString(R.string.empty_password))
            false
        } else {
            showErrorMessage(mAppContext.getString(R.string.empty_string))
            true
        }

    fun showErrorMessage(errorMessage: String) {
        mShowErrorMessage.value = errorMessage
    }

    private fun signUpUser() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mUserEmail, mUserPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
                    mSharedPrefs.loggedInUserId = userId
                    saveUserDataToFirebase(userId)
                }
            }
            .addOnFailureListener { exception ->
                mShowProgressBar.value = false
                showToast(exception.localizedMessage)
            }
    }

    private fun loginUser() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(mUserEmail, mUserPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
                    mSharedPrefs.loggedInUserId = userId
                    mShowProgressBar.value = false
                    mSharedPrefs.isUserLoggedIn = true
                    mStartTrainActivity.value = true
                }
            }.addOnFailureListener { exception ->
                mShowProgressBar.value = false
                showToast(exception.localizedMessage)
            }
    }

    private fun saveUserDataToFirebase(userId: String) {
        FirebaseFirestore.getInstance().collection(FcmDataProvider.userDataCollection)
            .document(userId).set(UserDataModel(mUserEmail, userId, mUserName))
            .addOnSuccessListener {
                mShowProgressBar.value = false
                mSharedPrefs.isUserLoggedIn = true
                mStartTrainActivity.value = true
            }
            .addOnFailureListener { exception ->
                mShowProgressBar.value = false
                showToast(exception.localizedMessage)
            }
    }
}