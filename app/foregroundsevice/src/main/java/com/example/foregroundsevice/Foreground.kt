package com.example.foregroundsevice  // ← 매니페스트의 package와 동일해야 함

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class Foreground : Service() {

    private val CHANNEL_ID = "ForegroundChannel" // 오탈자 정리

    override fun onBind(intent: Intent): IBinder = Binder()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        Log.d("ForegroundService", "createNotificationChannel() called")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java) // ✅ 올바른 타입
            Log.d("ForegroundService", "manager.createNotificationChannel called")
            manager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("ForegroundService", "onStartCommand() called")

        val notification: Notification =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText("running…")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setOngoing(true)
                .build()

        startForeground(1, notification)

        return START_STICKY
    }
}
