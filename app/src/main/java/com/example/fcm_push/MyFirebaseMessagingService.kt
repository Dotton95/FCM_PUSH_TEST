package com.example.fcm_push

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        const val TAG = "MessagingService"
        private const val CHANNEL_NAME = "Push Notification"
        private const val CHANNEL_DESCRIPTION = "Push Notification 을 위한 채널"
        private const val CHANNEL_ID = "Channel Id"
        private const val NOTIFICATION_ID = 1001
    }

    /* 토큰 생성 메서드 */
    override fun onNewToken(token: String) {
        Log.d(TAG,"new Token : $token")
        /*
        // 토큰 값을 따로 저장해둔다.
        val pref = this.getSharedPreferences("token", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("token", token).apply()
        editor.commit()

        Log.i("로그: ", "성공적으로 토큰을 저장함")
         */
    }

    /* 메세지 수신 메서드 */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        val title = remoteMessage.data["title"]
        val body = remoteMessage.data["body"]

        Log.d(TAG, "onMessageReceived() - from : ${remoteMessage.from}")
        Log.d(TAG, "onMessageReceived() - title : $title")
        Log.d(TAG, "onMessageReceived() - body : $body")

        sendNotification(title, body)
    }

    /* 알림 생성 메서드 */
    private fun sendNotification(title: String?, body: String?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //Oreo(26) 이상 버전에는 channel 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH )
            channel.description = CHANNEL_DESCRIPTION
            notificationManager.createNotificationChannel(channel)
        }
        //(System.currentTimeMillis()/100).toInt(),
        //알림 생성
        NotificationManagerCompat.from(this)
            .notify(1001, createNotification(title, body))  //알림이 여러개 표시되도록 requestCode 를 추가
    }


    /* 알림 설정 메서드 */
    private fun createNotification(title: String?, body: String?): Notification {

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP )
        }
        val pendingIntent = PendingIntent.getActivity(this, (System.currentTimeMillis()/100).toInt(), intent, FLAG_UPDATE_CURRENT)  //알림이 여러개 표시되도록 requestCode 를 추가

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)  //클릭 시 자동으로 삭제되도록 설정
            .setFullScreenIntent(pendingIntent,true)
            //.setContentIntent(pendingIntent)  //알림 눌렀을 때 실행할 Intent 설정

        return notificationBuilder.build()
    }
}