package com.example.gracepananggung

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlin.random.Random

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Pengingat"
        val message = intent.getStringExtra("message") ?: "Ada sesuatu yang harus kamu ingat."

        // Pastikan channel dibuat
        createNotificationChannel(context)

        val builder = NotificationCompat.Builder(context, "BOOK_REMINDER_CHANNEL")
            .setSmallIcon(R.drawable.ic_notification) // Pastikan drawable ini ada
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        //notificationManager.notify(Random.nextInt(0, Int.MAX_VALUE), builder.build())
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Pengingat Buku"
            val descriptionText = "Channel untuk pengingat buku"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("BOOK_REMINDER_CHANNEL", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
