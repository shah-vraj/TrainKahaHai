package com.example.trainkahahai

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import com.example.trainkahahai.adapter.SegmentControlAdapter
import com.example.trainkahahai.databinding.LayoutSegmentControlBinding
import com.example.trainkahahai.interfaces.SegmentClickListener

class SegmentControlView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) : FrameLayout(context!!, attrs) {

    private val mSegmentControlAdapter by lazy {
        SegmentControlAdapter(mutableListOf(), this::setOnSegmentSelected)
    }

    // View Binding
    private var mBinding: LayoutSegmentControlBinding
    private lateinit var mOnSegmentClickListener: SegmentClickListener
    private var mIsViewClickable = true

    init {
        mBinding = LayoutSegmentControlBinding.inflate(LayoutInflater.from(context), this, true)
        mBinding.recyclerView.apply {
            adapter = mSegmentControlAdapter
            itemAnimator = null
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return !mIsViewClickable
    }

    fun setSegments(segmentList: List<String>) {
        mSegmentControlAdapter.addSegments(segmentList)
    }

    fun setSelectedSegment(selectedSegment: String) {
        mSegmentControlAdapter.setSelectedSegment(selectedSegment)
    }

    fun setOnSegmentClickListener(onSegmentClickListener: SegmentClickListener) {
        mOnSegmentClickListener = onSegmentClickListener
    }

    fun setIsViewClickable(isClickable: Boolean) {
        mIsViewClickable = isClickable
    }

    private fun setOnSegmentSelected(selectedSegment: String) {
        if (this::mOnSegmentClickListener.isInitialized) {
            mOnSegmentClickListener.onSegmentSelected(selectedSegment)
        }
    }

    companion object {
        private val TAG = SegmentControlView::class.java.canonicalName
    }
}