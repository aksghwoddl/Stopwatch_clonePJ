package com.lee.mytimer.recyclerview

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lee.mytimer.R

class RecyclerViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    private val arrowImageView : ImageView = itemView.findViewById(R.id.arrowImageView)
    private val sectionTexView : TextView = itemView.findViewById(R.id.sectionViewHolderTextView)
    private val sectionRecordTextView : TextView = itemView.findViewById(R.id.sectionRecordViewHolderTextView)
    private val totalTimeTextView : TextView = itemView.findViewById(R.id.totalTimeViewHolderTextView)

    fun bind(model : RecyclerViewModel) {
        sectionTexView.text = convertIndexString(model.index)
        sectionRecordTextView.text = model.sectionTime
        totalTimeTextView.text = model.totalTime
    }

    /** For convert index to String **/
    private fun convertIndexString(index : Int) : String {
        return if(index + 1 < 10) "0${index + 1}" else (index + 1).toString()
    }

    fun getSectionTextView() = sectionTexView

    fun getArrowImageView() = arrowImageView
}
