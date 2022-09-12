package com.example.trainkahahai.model

import com.google.gson.annotations.SerializedName

data class NotificationModel(
    @SerializedName("to")
    val topic: String = "",
    @SerializedName("data")
    val notificationDataModel: NotificationDataModel
)
