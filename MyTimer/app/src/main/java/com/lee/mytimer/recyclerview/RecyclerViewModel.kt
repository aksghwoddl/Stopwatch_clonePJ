package com.lee.mytimer.recyclerview

import com.lee.mytimer.common.NORMAL_VALUE

data class RecyclerViewModel (
    var index : Int = 0 ,
    var sectionTime : String = "" ,
    var totalTime : String = "" ,
    var sectionRepeatedTime : Int = 0 ,
    var setSectionView : Int = NORMAL_VALUE
)