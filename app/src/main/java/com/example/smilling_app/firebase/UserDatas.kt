package com.example.smilling_app.firebase

import androidx.room.PrimaryKey
import com.google.type.Date
import com.google.type.DateTime

data class UserDatas(
    @PrimaryKey
    var id: String = "",
    var userDataId: String = "",
    var npkResult: String = "",
    var date: Date,
    var time: DateTime,
    var fertilizerId: String = ""
)
