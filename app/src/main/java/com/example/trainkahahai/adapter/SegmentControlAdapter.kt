package com.example.trainkahahai.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trainkahahai.databinding.ItemSegmentControlBinding
import com.example.trainkahahai.interfaces.SegmentClickListener

class SegmentControlAdapter(
    private val mSegmentList: MutableList<Segment>,
    private val mOnSegmentClickListener: SegmentClickListener
) : RecyclerView.Adapter<SegmentControlAdapter.SegmentControlHolder>() {

    inner class SegmentControlHolder(private val mViewBinding: ItemSegmentControlBinding) :
        RecyclerView.ViewHolder(mViewBinding.root) {

        fun bind(segment: Segment, clickListener: SegmentClickListener) {
            mViewBinding.segment = segment
            itemView.setOnClickListener {
                setSelectedSegment(segment.label)
                clickListener.onSegmentSelected(segment.label)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SegmentControlHolder {
        val binding: ItemSegmentControlBinding =
            ItemSegmentControlBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SegmentControlHolder(binding)
    }

    override fun onBindViewHolder(holder: SegmentControlHolder, position: Int) {
        holder.bind(mSegmentList[position], mOnSegmentClickListener)
    }

    override fun getItemCount(): Int = mSegmentList.size

    @SuppressLint("NotifyDataSetChanged")
    fun addSegments(segmentList: List<String>) {
        mSegmentList.clear()
        mSegmentList.addAll(segmentList.map { Segment(it) })
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedSegment(selectedSegment: String) {
        mSegmentList.forEach {
            it.isSelected = it.label == selectedSegment
        }
        notifyDataSetChanged()
    }

    data class Segment(
        val label: String,
        var isSelected: Boolean = false
    )
}