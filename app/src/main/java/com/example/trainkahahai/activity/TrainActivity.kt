package com.example.trainkahahai.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.trainkahahai.R
import com.example.trainkahahai.base_classes.BaseActivity
import com.example.trainkahahai.databinding.ActivityTrainBinding
import com.example.trainkahahai.obtainViewModel
import com.example.trainkahahai.viewmodel.TrainViewModel

class TrainActivity : BaseActivity() {

    private lateinit var mBinding: ActivityTrainBinding

    private val mTrainViewModel by lazy {
        obtainViewModel(TrainViewModel::class.java)
    }
    private val mStationsList by lazy {
        resources.getStringArray(R.array.stationsList)
    }
    private val mSpinnerAdapter by lazy {
        ArrayAdapter(this, R.layout.layout_station_spinner, R.id.stationName, mStationsList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityTrainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupTrainViewModel()
        setupUi()
    }

    private fun setupUi() {
        mBinding.spnStations.adapter = mSpinnerAdapter
        mBinding.spnStations.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                mTrainViewModel.setSelectedStation(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) { }
        }
    }

    private fun setupTrainViewModel() {
        mBinding.trainViewModel = mTrainViewModel
        mBinding.lifecycleOwner = this

        mTrainViewModel.selectedTrain.observe(this) {
            mTrainViewModel.updateCurrentStation()
        }
        mTrainViewModel.toastMessage.observe(this) {
            toastMessage(it)
        }
    }
}