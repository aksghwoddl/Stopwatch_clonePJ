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
import com.lee.mytimer.common.MAX_VALUE
import com.lee.mytimer.common.MIN_VALUE
import com.lee.mytimer.databinding.RecyclerViewHolderBinding


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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        Log.d(TAG , "onCreateViewHolder()")
        val binding = RecyclerViewHolderBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return RecyclerViewHolder(binding)
    }

    override fun getItemCount() = modelList.size


    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder()")
        setViewHolder(holder , position)
        holder.bind(modelList[position])
    }

    fun setList(list : MutableList<RecyclerViewModel>) {
        modelList = list
    }

    fun clearAll() {
        modelList.clear()
    }

    private fun setViewHolder(holder : RecyclerViewHolder , position : Int){
        // check whether last item is min or max
        if(position == itemCount -1){
            holder.itemView.animation = AnimationUtils.loadAnimation(context , R.anim.fade_in)
            if(modelList[position].setSectionView == MIN_VALUE){
                Log.d(TAG, "setSectionTextView: repeatedTime is min!!!")
                with(holder.getRecyclerViewHolderBinding()){
                    setViewHolderColor(this , Color.BLUE)
                    arrowImageView.setImageResource(R.drawable.ic_baseline_arrow_downward_24)
                    arrowImageView.animation = AnimationUtils.loadAnimation(context , R.anim.fade_out)
                }
            } else {
                if(modelList[position].setSectionView == MAX_VALUE){
                    Log.d(TAG, "setSectionTextView: repeatedTime is max!!!")
                    with(holder.getRecyclerViewHolderBinding()){
                        setViewHolderColor(this , Color.RED)
                        arrowImageView.setImageResource(R.drawable.ic_baseline_arrow_upward_24)
                        arrowImageView.animation = AnimationUtils.loadAnimation(context , R.anim.fade_out)
                    }
                }
            }
        }

        with(holder.getRecyclerViewHolderBinding()){
            // set previous sectionTextViews color
            when(modelList[position].setSectionView){
                MIN_VALUE -> setViewHolderColor(this , Color.BLUE)
                MAX_VALUE -> setViewHolderColor(this , Color.RED)
                else -> setViewHolderColor(this , context.getColor(R.color.gray))
            }

            // remove previous items arrowImageView
            if(arrowImageView.isVisible){
               arrowImageView.visibility = View.INVISIBLE
            }
        }
    }

    private fun setViewHolderColor(binding : RecyclerViewHolderBinding , color : Int) {
        with(binding){
            sectionViewHolderTextView.setTextColor(color)
            sectionRecordViewHolderTextView.setTextColor(color)
            totalTimeViewHolderTextView.setTextColor(color)
        }
    }

}