package com.example.smilling_app.firebase

import androidx.room.PrimaryKey
import com.google.type.Date
import com.google.type.DateTime

data class SensorDatas(
    var N : Double = 0.0,
    var P : Double = 0.0,
    var K : Double = 0.0,
    var pH: Double = 0.0,
    var Temp : Double = 0.0,
    var Cond : Double = 0.0,
    var Moist: Double = 0.0,
    var Waktu: String = ""
)
