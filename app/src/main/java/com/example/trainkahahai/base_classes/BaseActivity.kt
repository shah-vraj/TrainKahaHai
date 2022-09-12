package com.example.trainkahahai.base_classes

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trainkahahai.R
import com.example.trainkahahai.SharedPrefs

open class BaseActivity: AppCompatActivity() {

    fun dismissKeyboard() {
        currentFocus?.let {
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    fun startNewActivity(activity: Activity) =
        this.startActivity(Intent(this, activity::class.java))

    fun toastMessage(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    fun showDialog(error: String) {
        val alertDialogBuilder = AlertDialog.Builder(this).apply {
            setTitle(R.string.error_title)
            setMessage(error)
            setIcon(android.R.drawable.stat_sys_warning)
            setPositiveButton(getString(R.string.ok)) { _, _ -> }
        }
        alertDialogBuilder.create().apply {
            setCancelable(true)
            show()
        }
    }
}