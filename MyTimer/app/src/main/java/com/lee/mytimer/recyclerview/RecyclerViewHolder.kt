package com.lee.mytimer.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.lee.mytimer.databinding.RecyclerViewHolderBinding

class RecyclerViewHolder(private val binding : RecyclerViewHolderBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model : RecyclerViewModel) {
        with(binding){
            sectionViewHolderTextView.text = convertIndexString(model.index)
            sectionRecordViewHolderTextView.text = model.sectionTime
            totalTimeViewHolderTextView.text = model.totalTime
        }
    }

    /** For convert index to String **/
    private fun convertIndexString(index : Int) : String {
        return if(index + 1 < 10) "0${index + 1}" else (index + 1).toString()
    }

    fun getRecyclerViewHolderBinding() = binding
}
