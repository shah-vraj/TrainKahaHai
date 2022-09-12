package com.example.trainkahahai

import android.content.Context

class SharedPrefs(
    private val mAppContext: Context
) {

    private val mSharedPrefs = mAppContext.getSharedPreferences(
        mAppContext.getString(R.string.shared_prefs),
        Context.MODE_PRIVATE
    )
    private val mEditor by lazy {
        mSharedPrefs.edit()
    }

    var isNotificationTriggeredFromCurrentUser: Boolean
        get() = getBoolean(mAppContext.getString(R.string.check_if_user_in_app_key))
        set(value) = putBoolean(mAppContext.getString(R.string.check_if_user_in_app_key), value)

    var loggedInUserId: String
        get() = getString(mAppContext.getString(R.string.logged_in_user_id_key))
        set(value) = putString(mAppContext.getString(R.string.logged_in_user_id_key), value)

    var isUserLoggedIn: Boolean
        get() = getBoolean(mAppContext.getString(R.string.is_user_logged_in_key))
        set(value) = putBoolean(mAppContext.getString(R.string.is_user_logged_in_key), value)

    private fun getBoolean(key: String) = mSharedPrefs.getBoolean(key, false)

    private fun getString(key: String) = mSharedPrefs.getString(key, "") ?: ""

    private fun putBoolean(key: String, value: Boolean) {
        mEditor.apply {
            putBoolean(key, value)
            apply()
        }
    }

    private fun putString(key: String, value: String) {
        mEditor.apply {
            putString(key, value)
            apply()
        }
    }
}