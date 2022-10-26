package com.lee.mytimer

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.mytimer.common.MAX_VALUE
import com.lee.mytimer.common.MIN_VALUE
import com.lee.mytimer.databinding.ActivityMainBinding
import com.lee.mytimer.recyclerview.RecyclerAdapter
import com.lee.mytimer.recyclerview.RecyclerViewModel
import java.util.*
import kotlin.concurrent.timer
import kotlin.math.min

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    
    private lateinit var binding : ActivityMainBinding
    private lateinit var mRecyclerViewAdapter: RecyclerAdapter
    private var mMainTimeTask : Timer? = null

    /** For Control Button State **/
    private var mIsRunning = false
    private var mIsReset = false

    /** For Control Time Value **/
    private var mMainRepeatedTime = 0
    private var mSectionRepeatedTime = 0

    private val mModelList = mutableListOf<RecyclerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate()")
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        addListeners()
        initRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy()")
        mMainTimeTask = null
    }


    @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged")
    private fun addListeners() {
        with(binding){
            startStopButton.setOnClickListener {
                mIsRunning = !mIsRunning
                if(mIsRunning){
                    // when timer is running
                    start()
                } else {
                    // when timer is stopped
                    stop()
                }
            }
            recordingResetButton.setOnClickListener {
                if(mIsReset){
                    // when button show reset
                    reset()
                } else {
                    // when button show recording
                   record()
                }
            }
        }
    }

    private fun initRecyclerView() {
        mRecyclerViewAdapter = RecyclerAdapter.getInstance(this@MainActivity)
        binding.recordingList.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , true)
        binding.recordingList.adapter = mRecyclerViewAdapter
    }

    /** StopWatch Control Functions Start **/

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun start() {
        with(binding){
            with(startStopButton){
                text = getString(R.string.stop)
                background = getDrawable(R.drawable.round_button_stop)
            }
            sectionRecordTextView.isVisible = true
            if(!recordingResetButton.isEnabled) recordingResetButton.isEnabled = true

            recordingResetButton.text = getString(R.string.record)
        }

        mMainTimeTask = timer(period = 10){
           timerStart()
        }
        mIsReset = false
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun stop() {
        with(binding){
            with(startStopButton){
                text = getString(R.string.start)
                background = getDrawable(R.drawable.round_button_start)
            }
            recordingResetButton.text = getString(R.string.reset)
        }
        timerStop()
        mIsReset = true
    }

    @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged")
    private fun reset() {
        with(binding){
            recordingLayout.visibility = View.GONE
            with(recordingResetButton){
                text = getString(R.string.record)
                isEnabled = false
                with(startStopButton){
                    text = getString(R.string.start)
                    background = getDrawable(R.drawable.round_button_start)
                }

            }
            timeTextView.text = getString(R.string.init_time)
            timerStop()
            mMainRepeatedTime = 0
            mSectionRepeatedTime = 0
            sectionRecordTextView.isVisible = false
        }
        //Recording data clear
        mModelList.clear()
        mRecyclerViewAdapter.clearAll()
        mRecyclerViewAdapter.notifyDataSetChanged()
        mMainTimeTask = null
    }
    
    @SuppressLint("NotifyDataSetChanged")
    private fun record() {
        addRecordingData()
        mRecyclerViewAdapter.setList(mModelList)
        mRecyclerViewAdapter.notifyDataSetChanged()
        mSectionRepeatedTime = 0
        binding.run {
            sectionRecordTextView.text = getString(R.string.init_time)
            recordingList.smoothScrollToPosition(mModelList.size)
        }
       /* mModelList.forEach{ // For Debug
            Log.d(TAG, "record: ${it.index} , ${it.sectionTime} , ${it.totalTime} , ${it.sectionRepeatedTime}")
        }*/

    }

    /** StopWatch Control Functions End **/

    /** Time Format change when first character  is '0' **/
    private fun changeTimeToString(value : Int , needCheck_60 : Boolean) : String{
        if(value < 10){
            return "0$value"
        } else {
            if(needCheck_60){
                if (value >= 60){
                    return "00"
                }
            }
            return value.toString()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun timerStart() {
        mMainRepeatedTime++
        mSectionRepeatedTime++
        // values of time components start
        val hour = mMainRepeatedTime / 360000
        var minute = mMainRepeatedTime / 6000
        var seconds = mMainRepeatedTime / 100
        val milliseconds =  mMainRepeatedTime % 100

        val sectionHour = mSectionRepeatedTime / 360000
        var sectionMinute = mSectionRepeatedTime / 6000
        var sectionSeconds = mSectionRepeatedTime / 100
        val sectionMilliseconds =  mSectionRepeatedTime % 100
        // values of time components end

        // For change values when when seconds or minutes over 60
        if(seconds >= 60){
            seconds = 0
        }
        if(minute >= 60){
            minute = 0
        }
        if(sectionSeconds >= 60){
            sectionSeconds = 0
        }
        if(sectionMinute >= 60){
            sectionMinute = 0
        }

        val millisecondsText = changeTimeToString(milliseconds , false)
        val secondsText = changeTimeToString(seconds , true)
        val minuteText = changeTimeToString(minute , true)
        val hourText = changeTimeToString(hour , false)

        val sectionMillisecondsText = changeTimeToString(sectionMilliseconds , false)
        val sectionSecondsText = changeTimeToString(sectionSeconds , true)
        val sectionMinuteText = changeTimeToString(sectionMinute , true)
        val sectionHourText = changeTimeToString(sectionHour , false)

        runOnUiThread{
            binding.run {
                timeTextView.text = "${hourText}:${minuteText}:${secondsText}.${millisecondsText}"
                sectionRecordTextView.text = "${sectionHourText}:${sectionMinuteText}:${sectionSecondsText}.${sectionMillisecondsText}"
            }
        }
    }

    private fun timerStop() {
        mMainTimeTask?.cancel()
    }

    /** Function for add Recording Data **/
    private fun addRecordingData() {
        val recodingTimeData = RecyclerViewModel(
            mRecyclerViewAdapter.itemCount ,
            binding.sectionRecordTextView.text.toString() ,
            binding.timeTextView.text.toString() ,
            mSectionRepeatedTime
        )
        if(mModelList.isEmpty()){ // Loading animation when show record list at first
            with(binding.recordingLayout){
                visibility = View.VISIBLE
                animation = AnimationUtils.loadAnimation(this@MainActivity , R.anim.slide_down_to_up)
            }
        } else { // Check whether sectionRepeatedTime is min or max
            val minValue = mModelList.minBy { it.sectionTime }
            val maxValue = mModelList.maxBy { it.sectionTime }
            if(recodingTimeData.sectionTime < minValue.sectionTime){
                recodingTimeData.setSectionView = MIN_VALUE
                minValue.setSectionView = -1
            } else if(recodingTimeData.sectionTime > maxValue.sectionTime){
                recodingTimeData.setSectionView = MAX_VALUE
                maxValue.setSectionView = -1
            }
        }

        mModelList.add(recodingTimeData)
    }
}