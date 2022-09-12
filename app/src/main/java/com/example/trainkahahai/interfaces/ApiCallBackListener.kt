package com.example.trainkahahai.interfaces

import com.example.trainkahahai.model.ErrorResponse

interface ApiCallBackListener {
    fun<T: Any> onSuccess(data: T)
    fun onFailure(error: ErrorResponse)
}