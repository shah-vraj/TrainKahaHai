package com.example.trainkahahai.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trainkahahai.FcmDataProvider
import com.example.trainkahahai.R
import com.example.trainkahahai.SharedPrefs
import com.example.trainkahahai.base_classes.BaseViewModel
import com.example.trainkahahai.getNonNullValue
import com.example.trainkahahai.interfaces.ApiCallBackListener
import com.example.trainkahahai.interfaces.RetrofitBuilder
import com.example.trainkahahai.model.NotificationDataModel
import com.example.trainkahahai.model.NotificationModel
import com.example.trainkahahai.model.ErrorResponse
import com.example.trainkahahai.model.TrainDataModel

class TrainViewModel(
    private val mAppContext: Application,
    private val mSharedPrefs: SharedPrefs
): BaseViewModel() {

    private val mListOfTrains by lazy {
        mAppContext.resources.getStringArray(R.array.trainList).toList()
    }
    private val mRetrofitBuilder by lazy {
        RetrofitBuilder.getRetrofitObject().create(RetrofitBuilder::class.java)
    }

    private val mTrainsList: MutableLiveData<List<String>> = MutableLiveData(mListOfTrains)
    val trainsList: LiveData<List<String>> get() = mTrainsList

    private val mSelectedTrain = MutableLiveData(mListOfTrains[0])
    val selectedTrain: LiveData<String> get() = mSelectedTrain

    private val mCurrentTrainStation = MutableLiveData(TrainStation.NONE)
    val currentTrainStation: LiveData<TrainStation> get() = mCurrentTrainStation

    private val mShowProgressBar = MutableLiveData(false)
    val showProgressBar: LiveData<Boolean> get() = mShowProgressBar

    private val mTrainKey: String
        get() = when (selectedTrain.value) {
            mListOfTrains[0] -> mAppContext.getString(R.string.key_sankalp)
            mListOfTrains[1] -> mAppContext.getString(R.string.key_queen)
            mListOfTrains[2] -> mAppContext.getString(R.string.key_memu_return)
            mListOfTrains[3] -> mAppContext.getString(R.string.key_janta)
            mListOfTrains[4] -> mAppContext.getString(R.string.key_lokshakti)
            else -> mAppContext.getString(R.string.key_sankalp)
        }

    private var mSelectedStation = TrainStation.NONE

    fun updateCurrentStation() {
        mShowProgressBar.value = true
        getTrainData(mTrainKey,
            onSuccessListener = {
                it?.currentStation?.let { currentStation ->
                    mCurrentTrainStation.value = TrainStation.getTrainStationFromValue(currentStation)
                    mShowProgressBar.value = false
                } ?: kotlin.run {
                    mShowProgressBar.value = false
                    showToast(mAppContext.getString(R.string.cannot_find_data, mSelectedTrain.value))
                }
            }, onFailureListener = { exception ->
                mShowProgressBar.value = false
                showToast(exception)
            }
        )
    }

    fun onTrainSelected(selectedTrain: String) {
        mSelectedTrain.value = selectedTrain
    }

    fun setSelectedStation(selectedTrainIndex: Int) {
        mSelectedStation = TrainStation.values()[selectedTrainIndex]
    }

    fun updateTrainStatus() {
        mShowProgressBar.value = true
        setTrainData(mTrainKey, TrainDataModel(mSelectedStation.station),
            onSuccessListener = {
                mShowProgressBar.value = false
                if (mCurrentTrainStation.value != mSelectedStation && mSelectedStation != TrainStation.NONE) {
                    sendNotification(
                        mSelectedTrain.getNonNullValue(),
                        mAppContext.getString(R.string.notification_message, mSelectedStation.station)
                    )
                }
                mCurrentTrainStation.value = mSelectedStation
            }, onFailureListener = { exception ->
                mShowProgressBar.value = false
                showToast(exception)
            }
        )
    }

    private fun sendNotification(title: String, message: String) {
        val notificationDataModel = NotificationDataModel(title, message)
        val notificationModel =
            NotificationModel(FcmDataProvider.topicForAll, notificationDataModel)
        mSharedPrefs.isNotificationTriggeredFromCurrentUser = true

        call(
            mRetrofitBuilder.registerDevice(getHeadersForFcm(), notificationModel),
            object : ApiCallBackListener {
                override fun <T : Any> onSuccess(data: T) {
                    showToast(mAppContext.getString(R.string.update_successful))
                }

                override fun onFailure(error: ErrorResponse) {
                    showToast(mAppContext.getString(R.string.update_unsuccessful, error.error))
                }
            }
        )
    }

    private fun getHeadersForFcm(): Map<String, String> =
        mutableMapOf<String, String>().apply {
            put("Authorization", FcmDataProvider.serverKey)
            put("Content-Type", FcmDataProvider.contentType)
        }

    enum class TrainStation(val station: String) {
        NONE("none"),
        ANAND("Anand"),
        NADIAD("Nadiad"),
        MAHEMDABAD("Mahemdabad"),
        MANINAGAR("Maninagar"),
        AHMEDABAD("Ahmedabad");

        companion object {
            fun getTrainStationFromValue(value: String): TrainStation {
                values().forEach {
                    if (it.station == value) return it
                }
                return NONE
            }
        }
    }
}