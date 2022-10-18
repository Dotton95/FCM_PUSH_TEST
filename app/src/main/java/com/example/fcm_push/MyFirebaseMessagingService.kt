package com.example.fcm_push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        const val TAG = "MessagingService"
        private var CHANNEL_NAME = "Push Notification Channel"
        private const val CHANNEL_DESCRIPTION = "Push Notification 을 위한 채널"
        private  const val CHANNEL_ID = "CHANNEL_ID"
        private const val NOTIFICATION_ID = 1001
    }

    /* 토큰 생성 메서드 */
    override fun onNewToken(token: String) {
        Log.d(TAG,"new Token : $token")
        sendTokenToServer()

    }
    /* 토큰값 서버에 저장하기 */
    private fun sendTokenToServer() {
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
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var noti_title = remoteMessage.notification!!.title.toString()
        var noti_body = remoteMessage.notification!!.body.toString()

        var data_title = remoteMessage.data["title"].toString()
        var data_body = remoteMessage.data["body"].toString()

        /* 알림 빌더 */
        val notificationBuilder = NotificationCompat.Builder(this,CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)//아이콘
            .setContentTitle(noti_title)
            .setContentText(noti_body)
            .setAutoCancel(true) //클릭 시 자동으로 삭제되도록 설정

        //Oreo(26) 이상 버전에는 channel 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            channel.description = CHANNEL_DESCRIPTION
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID,notificationBuilder.build())
    }
}