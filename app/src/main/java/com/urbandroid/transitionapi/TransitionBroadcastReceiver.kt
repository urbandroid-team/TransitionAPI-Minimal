package com.urbandroid.transitionapi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

import com.google.android.gms.location.ActivityTransitionEvent
import com.google.android.gms.location.ActivityTransitionResult

class TransitionBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (ActivityTransitionResult.hasResult(intent)) {
            val result = ActivityTransitionResult.extractResult(intent)
            if (result != null) {
                for (event in result.transitionEvents) {
                    val ew = ActivityTransitionEventWrapper(event)

                    val i = Intent(ACTION)
                    i.putExtra("event", ew.eventDisplayFormat)
                    i.setPackage(context.packageName)
                    context.sendBroadcast(i)
                    Log.i("AR", "A " + ew.eventDisplayFormat)
                }
            }
        }
    }

    companion object {

        var ACTION = "com.urbandroid.activity_transition"
    }
}
