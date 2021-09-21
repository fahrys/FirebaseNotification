package com.example.myfirebasepushnotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationId = 101
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d(TAG , "From: ${remoteMessage.from}")

        if (remoteMessage.data.size > 0) {
            val body = remoteMessage.data["body"]
            val title = remoteMessage.data["title"]
            showNotification(applicationContext , title , body)

            Log.d(TAG , "Message Notification Body: $body")
        } else {
            val tile = remoteMessage.notification!!.title
            val body = remoteMessage.notification!!.body
            showNotification(applicationContext , tile , body)
        }
        remoteMessage.notification?.let {
            Log.d(TAG , "Message Notification Body: ${it.body}")
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d(TAG , "Refreshed token : $p0")
    }

    fun showNotification(context: Context , title:String? , message:String?) {
        val ii : Intent
        ii = Intent(context , MainActivity::class.java)
        ii.data = (Uri.parse("costum//" + System.currentTimeMillis()))
        ii.action = "actionstring" + System.currentTimeMillis()
        ii.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pi = PendingIntent.getActivity(context , 0 , ii ,
        PendingIntent.FLAG_UPDATE_CURRENT)
        val notification:Notification
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setWhen(System.currentTimeMillis())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title).build()

            val notificationManager = context.getSystemService(
             Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            val notificationChannel = NotificationChannel(
                CHANNEL_ID , title , NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationManager.createNotificationChannel(notificationChannel)
            notificationManager.notify(notificationId , notification)
        } else {
            notification = NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(message)
                .setContentIntent(pi)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title).build()

            val notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.notify(notificationId , notification)
        }
    }
    companion object {
        const val TAG = "Token"
    }
}