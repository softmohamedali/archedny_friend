package com.example.archedny_app_friend.services

import android.content.Intent
import androidx.lifecycle.LifecycleService
import com.example.archedny_app_friend.utils.Constants

//i use LifeCycleService to be aware of lifecyle beacuse when i use mutalbeflow or livedata
class TrackingService:LifecycleService() {

    // this function excute in every intetn or command send to the sevices
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let{
            when(intent.action){
                Constants.START_SERVICES ->{

                }
                Constants.STOP_SERVICES->{

                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

}