package com.example.trainkahahai.interfaces

import com.example.trainkahahai.FcmDataProvider
import com.example.trainkahahai.model.NotificationModel
import com.example.trainkahahai.model.DeviceRegistrationResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface RetrofitBuilder {

    @POST(FcmDataProvider.fcmSendMessageEndUrl)
    fun registerDevice(
        @HeaderMap headers: Map<String, String>,
        @Body notificationModel: NotificationModel
    ): Call<DeviceRegistrationResponse>

    companion object {
        fun getRetrofitObject(): Retrofit {
            val okHttpClientBuilder = OkHttpClient.Builder()
            val okHttpClient = okHttpClientBuilder.build()

            return Retrofit.Builder()
                .baseUrl(FcmDataProvider.fcmBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}