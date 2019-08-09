package com.urbandroid.transitionapi

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.gms.location.DetectedActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*

import java.util.ArrayList

class MainActivity : Activity() {

    internal var detectedActivity = intArrayOf(DetectedActivity.IN_VEHICLE, DetectedActivity.ON_BICYCLE,
            // DetectedActivity.ON_FOOT,
            DetectedActivity.RUNNING, DetectedActivity.STILL,
            // DetectedActivity.TILTING,
            // DetectedActivity.UNKNOWN,
            DetectedActivity.WALKING)

    private val transitionActivityList: List<ActivityTransition>
        get() {
            val transitions = ArrayList<ActivityTransition>()
            for (activity in detectedActivity) {
                transitions.add(
                        ActivityTransition.Builder()
                                .setActivityType(activity)
                                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                                .build())
                transitions.add(
                        ActivityTransition.Builder()
                                .setActivityType(activity)
                                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                                .build())
            }
            return transitions
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvLog : TextView = findViewById(R.id.tvLog)

        val intent = Intent(this, TransitionBroadcastReceiver::class.java)
        val pendingIntentBroadcast = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val transitions = transitionActivityList
        val request = ActivityTransitionRequest(transitions)

        startGetBroadcast(pendingIntentBroadcast, request, "pendingIntentBroadcast")

        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (tvLog.text.length > 5000) {
                    tvLog.text = ""
                }

                tvLog.text = intent.getStringExtra("event") + "\n\n" + tvLog.text

            }
        }, IntentFilter(TransitionBroadcastReceiver.ACTION))

    }

    private fun startGetBroadcast(pendingIntent: PendingIntent, request: ActivityTransitionRequest, type: String) {
        // myPendingIntent is the instance of PendingIntent where the app receives callbacks.
        val task = ActivityRecognition.getClient(this).requestActivityTransitionUpdates(request, pendingIntent)
        task.addOnSuccessListener { Toast.makeText(this, "Waiting for Activity Transitions...", Toast.LENGTH_LONG).show() }
        task.addOnCompleteListener {
            //Toast.makeText(context, "oncomplete " + type, Toast.LENGTH_SHORT).show();
        }
        task.addOnFailureListener { e -> Toast.makeText(this , "Error : $e", Toast.LENGTH_LONG).show() }
    }

}
