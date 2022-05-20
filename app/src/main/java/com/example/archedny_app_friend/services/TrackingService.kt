package com.example.archedny_app_friend.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.archedny_app_friend.R
import com.example.archedny_app_friend.presentation.MainActivity
import com.example.archedny_app_friend.utils.Constants
import com.example.archedny_app_friend.utils.PermissionUtitlity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.type.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.flow.collect

//i use LifeCycleService to be aware of lifecyle beacuse when i use mutalbeflow or livedata
class TrackingService:LifecycleService() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object{
        val isTracking= MutableStateFlow(false)
        val currentLocation= MutableStateFlow<Location?>(null)
    }

    override fun onCreate() {
        super.onCreate()
        lifecycleScope.launchWhenStarted {
            isTracking.collect{
                updateLocationTracking(it)
            }
        }
        fusedLocationProviderClient=FusedLocationProviderClient(this)
    }

    // this function excute in every intetn or command send to the sevices
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let{
            when(intent.action){
                Constants.START_SERVICES ->{
                    startForeGroundService()
                }
                Constants.STOP_SERVICES->{
                    isTracking.value=false
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("MissingPermission")
    fun updateLocationTracking(isTracking:Boolean){
        if (isTracking){
            if (PermissionUtitlity.hasLocationPermissin(this)){
                val request=LocationRequest().apply {
                    interval=Constants.LOCATION_UPDATE_INTERVAL
                    fastestInterval=Constants.FASTEST_LOCATION_INTERVAL
                    priority=PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCalback,
                    Looper.getMainLooper()
                )
            }
        }else{
            fusedLocationProviderClient.removeLocationUpdates(
                locationCalback
            )
        }
    }

    val locationCalback=object :LocationCallback(){

        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (isTracking.value){
                result.locations.forEach {
                    lifecycleScope.launchWhenStarted {
                        currentLocation.emit(it)
                    }
                }
            }
        }

    }


    fun startForeGroundService(){
        isTracking.value=true
        val notificationManger=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotifictionChannel(notificationManger)
        val notificationBuilder=NotificationCompat.Builder(this,Constants.NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_marker)
            .setContentTitle("Archedny")
            .setContentText("maps")
            .setContentIntent(PendingIntent.getActivity(
                this,
                1,
                Intent(this,MainActivity::class.java),
                FLAG_UPDATE_CURRENT
            ))
        startForeground(
            Constants.NOTIFICATION_ID,
            notificationBuilder.build()
        )
    }

    fun createNotifictionChannel(notificationManger:NotificationManager){
        val notificationChannel=NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManger.createNotificationChannel(notificationChannel)
    }

}