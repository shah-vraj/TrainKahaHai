package com.example.trainkahahai.base_classes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trainkahahai.FcmDataProvider
import com.example.trainkahahai.interfaces.ApiCallBackListener
import com.example.trainkahahai.model.ErrorResponse
import com.example.trainkahahai.model.TrainDataModel
import com.example.trainkahahai.model.UserDataModel
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseViewModel: ViewModel() {

    private val mToastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = mToastMessage

    fun showToast(message: String?) {
        mToastMessage.value = message ?: ""
    }

    fun<T: Any> call(retrofitCall: Call<T>, apiCallBackListener: ApiCallBackListener) {
        retrofitCall.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        apiCallBackListener.onSuccess(it)
                    }
                } else {
                    apiCallBackListener.onFailure(ErrorResponse(response.code().toString()))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                apiCallBackListener.onFailure(ErrorResponse(t.toString()))
            }
        })
    }

    fun getUserData(
        userId: String,
        onSuccessListener: (UserDataModel?) -> Unit,
        onFailureListener: (String?) -> Unit
    ) {
        FirebaseFirestore.getInstance().collection(FcmDataProvider.userDataCollection)
            .document(userId).get()
            .addOnSuccessListener {
                onSuccessListener(it.toObject(UserDataModel::class.java))
            }
            .addOnFailureListener { exception ->
                onFailureListener(exception.localizedMessage)
            }
    }

    fun getTrainData(
        trainKey: String,
        onSuccessListener: (TrainDataModel?) -> Unit,
        onFailureListener: (String?) -> Unit
    ) {
        FirebaseFirestore.getInstance().collection(FcmDataProvider.trainDataCollection)
            .document(trainKey).get()
            .addOnSuccessListener {
                onSuccessListener(it.toObject(TrainDataModel::class.java))
            }
            .addOnFailureListener { exception ->
                onFailureListener(exception.localizedMessage)
            }
    }

    fun setTrainData(
        trainKey: String,
        trainDataModel: TrainDataModel,
        onSuccessListener: () -> Unit,
        onFailureListener: (String?) -> Unit
    ) {
        FirebaseFirestore.getInstance().collection(FcmDataProvider.trainDataCollection)
            .document(trainKey).set(trainDataModel)
            .addOnSuccessListener {
                onSuccessListener()
            }
            .addOnFailureListener { exception ->
                onFailureListener(exception.localizedMessage)
            }
    }
}