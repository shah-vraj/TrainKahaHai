package com.example.trainkahahai

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.trainkahahai.activity.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var mIntent: Intent
    private lateinit var mPendingIntent: PendingIntent

    private val mSharedPrefs by lazy {
        SharedPrefs(applicationContext)
    }
    private val mNotificationId by lazy {
        Random().nextInt(3000)
    }
    private val mNotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    private val mNotificationSoundUri by lazy {
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    }
    private val mLargeIcon by lazy {
        BitmapFactory.decodeResource(resources, R.drawable.ic_app_logo)
    }
    private val mNotificationChannel by lazy {
        NotificationChannel(
            FcmDataProvider.adminChannelId,
            FcmDataProvider.adminChannelName,
            NotificationManager.IMPORTANCE_HIGH
        )
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        setupIntents()
        setupNotificationChannel(mNotificationManager)
        if (!mSharedPrefs.isNotificationTriggeredFromCurrentUser) {
            mNotificationManager.notify(mNotificationId, getNotificationInstance(message))
        } else {
            mSharedPrefs.isNotificationTriggeredFromCurrentUser = false
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "New token: $token")
        super.onNewToken(token)
    }

    private fun setupIntents() {
        mIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        mPendingIntent = PendingIntent.getActivity(
            this,
            0,
            mIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun setupNotificationChannel(notificationManager: NotificationManager?) {
        mNotificationChannel.apply {
            description = FcmDataProvider.notificationChannelDescription
            enableLights(true)
            lightColor = Color.WHITE
            enableVibration(true)
            notificationManager?.createNotificationChannel(this)
        }
    }

    private fun getNotificationInstance(message: RemoteMessage): Notification {
        val notificationBuilder = NotificationCompat
            .Builder(this, FcmDataProvider.adminChannelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(mLargeIcon)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setAutoCancel(true)
            .setSound(mNotificationSoundUri)
            .setContentIntent(mPendingIntent)
            .setColor(ContextCompat.getColor(this, R.color.teal_200))
        return notificationBuilder.build()
    }

    companion object {
        private val TAG = MyFirebaseMessagingService::class.java.canonicalName
    }
}