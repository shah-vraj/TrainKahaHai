package com.example.trainkahahai.model

import com.google.gson.annotations.SerializedName

data class NotificationDataModel(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("message")
    val message: String = ""
)