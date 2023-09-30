package com.example.chatapplication

import java.text.SimpleDateFormat
import java.util.Locale


fun formatDate(timeStamp:Long):String{
    return SimpleDateFormat("MMM d\nh:mm a", Locale.ENGLISH).format(timeStamp)
}
