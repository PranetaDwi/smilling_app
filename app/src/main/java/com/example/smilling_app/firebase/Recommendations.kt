package com.example.smilling_app.firebase

import androidx.room.PrimaryKey
import com.google.type.Date
import com.google.type.DateTime

data class Recommendations(
    @PrimaryKey
    var id: String = "",
    var userDataId: String = "",
    var nitrogen: Int,
    var fosfor: Int,
    var kalium: Int,
    var ph: Int,
    var kelembaban: Int,
    var date: Date,
    var time: DateTime
)
