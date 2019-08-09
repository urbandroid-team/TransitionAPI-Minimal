package com.urbandroid.transitionapi

import com.google.android.gms.location.ActivityTransitionEvent
import com.google.android.gms.location.DetectedActivity

import java.text.Format
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap

import com.google.android.gms.location.ActivityTransition.ACTIVITY_TRANSITION_ENTER
import com.google.android.gms.location.ActivityTransition.ACTIVITY_TRANSITION_EXIT

internal class ActivityTransitionEventWrapper(private val event: ActivityTransitionEvent) {
    private var timestamp: Long = 0

    // text += event.getElapsedRealTimeNanos() +"\n";
    val eventDisplayFormat: String
        get() {
            val datetime = convertTime(timestamp)
            var text = datetime + "\n"
            text += getActivityTypeDesc(event.activityType)!! + "\n"
            text += getTransitionTypeDesc(event.transitionType)!! + "\n"
            return text
        }

    init {
        this.timestamp = System.currentTimeMillis()
    }

    private fun convertTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return format.format(date)
    }

    companion object {

        private val activityTypeMap = HashMap<Int, String>()
        private val transitionTypeMap = HashMap<Int, String>()

        private fun getActivityTypeDesc(type: Int): String? {
            if (activityTypeMap.size == 0) {
                activityTypeMap[DetectedActivity.IN_VEHICLE] = "IN_VEHICLE"
                activityTypeMap[DetectedActivity.ON_BICYCLE] = "ON_BICYCLE"
                activityTypeMap[DetectedActivity.RUNNING] = "RUNNING"
                activityTypeMap[DetectedActivity.STILL] = "STILL"
                activityTypeMap[DetectedActivity.WALKING] = "WALKING"
            }
            return activityTypeMap[type]
        }

        private fun getTransitionTypeDesc(type: Int): String? {
            if (transitionTypeMap.size == 0) {
                transitionTypeMap[ACTIVITY_TRANSITION_ENTER] = "ACTIVITY_TRANSITION_ENTER"
                transitionTypeMap[ACTIVITY_TRANSITION_EXIT] = "ACTIVITY_TRANSITION_EXIT"
            }
            return transitionTypeMap[type]
        }
    }

}
