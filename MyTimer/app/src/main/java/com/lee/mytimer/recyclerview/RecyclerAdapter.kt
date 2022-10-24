package com.lee.mytimer.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.lee.mytimer.R


class RecyclerAdapter(private val context : Context) : RecyclerView.Adapter<RecyclerViewHolder>() {
    private val TAG = "RecyclerAdapter"

    companion object{ //
        @SuppressLint("StaticFieldLeak")
        private lateinit var instance : RecyclerAdapter
        fun getInstance(context : Context) : RecyclerAdapter {
            if(!::instance.isInitialized){
                instance = RecyclerAdapter(context)
            }
            return instance
        }
    }

    private var modelList = mutableListOf<RecyclerViewModel>()

    private var minIndex = 0
    private var maxIndex = -1

    private lateinit var minValue : String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        Log.d(TAG , "onCreateViewHolder()")
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_view_holder , parent ,false)
        return RecyclerViewHolder(view)
    }

    override fun getItemCount() = modelList.size


    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder()")
        setSectionTextView(holder , position)
        holder.bind(modelList[position])
    }

    fun setList(list : MutableList<RecyclerViewModel>) {
        modelList = list
    }

    fun clearAll() {
        modelList.clear()
    }

    /** Check rep time is man **/
    private fun checkRepTimeMax(repeatedTime : Int) : Boolean {
        var max = 0
        modelList.forEach {
            if(it.sectionRepeatedTime > max){
                max = it.sectionRepeatedTime
            }
        }
        Log.d(TAG, "checkSectionMax: max is $max")
       return repeatedTime >= max
    }

    /** Check rep time is min **/
    private fun checkRepTimeMin(repeatedTime : Int) : Boolean {
        if(modelList.size == 1){
            minValue = repeatedTime.toString()
        }
        modelList.forEach {
            if(it.sectionRepeatedTime < minValue.toInt()){
               minValue = it.sectionRepeatedTime.toString()
            }
        }
        Log.d(TAG, "checkSectionMin: min is $minValue")
        return repeatedTime <= minValue.toInt()
    }

    private fun setSectionTextView(holder : RecyclerViewHolder , position : Int){
        // check whether last item is min or max
        if(position == itemCount -1){
            holder.itemView.animation = AnimationUtils.loadAnimation(context , R.anim.fade_in)
            if(checkRepTimeMin(modelList[position].sectionRepeatedTime)){
                Log.d(TAG, "setSectionTextView: repeatedTime is min!!!")
                holder.getSectionTextView().setTextColor(Color.BLUE)
                holder.getArrowImageView().setImageResource(R.drawable.ic_baseline_arrow_downward_24)
                holder.getArrowImageView().animation = AnimationUtils.loadAnimation(context , R.anim.fade_out)
                minIndex = position
            } else {
                if(checkRepTimeMax(modelList[position].sectionRepeatedTime)){
                    Log.d(TAG, "setSectionTextView: repeatedTime is max!!!")
                    holder.getSectionTextView().setTextColor(Color.RED)
                    holder.getArrowImageView().setImageResource(R.drawable.ic_baseline_arrow_upward_24)
                    holder.getArrowImageView().animation = AnimationUtils.loadAnimation(context , R.anim.fade_out)
                    maxIndex = position
                }
            }
        }

        // set previous sectionTextViews color
        when(position){
            minIndex -> holder.getSectionTextView().setTextColor(Color.BLUE)
            maxIndex ->holder.getSectionTextView().setTextColor(Color.RED)
            else -> holder.getSectionTextView().setTextColor(context.getColor(R.color.gray))
        }

        // remove previous items arrowImageView
        if(holder.getArrowImageView().isVisible){
            holder.getArrowImageView().visibility = View.INVISIBLE
        }
    }

}