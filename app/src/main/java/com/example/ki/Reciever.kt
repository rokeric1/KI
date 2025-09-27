package com.example.ki

//import android.app.Notification
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//import androidx.core.app.NotificationCompat
//
//class NotificationReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context, intent: Intent) {
//        // When the alarm goes off, trigger the notification
//        createNotification(context)
//    }
//    private fun createNotification(context: Context) {
//        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val channelId = "your_channel_id"
//
//        // Create notification channel for Android 8.0+ (Oreo and above)
//
//            val channel = NotificationChannel(
//                channelId,
//                "Channel Name",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            notificationManager.createNotificationChannel(channel)
//
//
//        // Build the notification
//        val notification: Notification = NotificationCompat.Builder(context, channelId)
//            .setContentTitle("KI DANA")
//            .setContentText("Dilbere jesi poslo za danas?")
//            .setSmallIcon(R.drawable.kontiki) // Set a small icon
//            .build()
//
//        // Trigger the notification
//        notificationManager.notify(0, notification)
//    }
//}

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("notificationId", 0)
        val title = intent.getStringExtra("title")
        val message = intent.getStringExtra("message")

        val notification = NotificationCompat.Builder(context, "default_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Use your app's icon here
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val manager = NotificationManagerCompat.from(context)
        manager.notify(notificationId, notification)
    }
}