package com.lee.mytimer.recyclerview

data class RecyclerViewModel (
    var index : Int = 0 ,
    var sectionTime : String = "" ,
    var totalTime : String = "" ,
    var sectionRepeatedTime : Int = 0
)