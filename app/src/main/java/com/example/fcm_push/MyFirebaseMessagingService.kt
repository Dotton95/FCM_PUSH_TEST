package com.example.fcm_push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        const val TAG = "MessagingService"
        private var CHANNEL_NAME = "Push Notification Channel"
        private const val CHANNEL_DESCRIPTION = "Push Notification 을 위한 채널"
        private  const val CHANNEL_ID = "CHANNEL_ID"
        private const val NOTIFICATION_ID = 1001
        private const val icon = R.drawable.ic_launcher_foreground
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

        Log.d(TAG,"remoteMessage.data : ${remoteMessage.data}")
        Log.d(TAG,"remoteMessage.notification : ${remoteMessage.notification}")

        /**(주의 : 모든 메시지는 수신된 지 20초 이내에 처리되어야 함)
         * notification - 앱이 백그라운드에 있을시 알림이 작업표시줄에 뜨기만 할뿐임
         * data - 앱이 포그라운드든 백그라운드든 일단 onMessageReceived로 들어감
         * */
        val title = remoteMessage.data["title"].toString()
        val body = remoteMessage.data["body"].toString()
        val image_url = remoteMessage.data["image"].toString()
        var image:Bitmap = BitmapFactory.decodeResource(resources, R.drawable.asd)

//        if (image_url.isNotEmpty()){
//            CoroutineScope(Dispatchers.Main).launch {
//                image =  withContext(Dispatchers.IO){
//                    ImageLoader.loadImage(image_url)
//                }
//            }
//        }
//        Glide.with(this).asBitmap().load(image_url).into(
//            object : CustomTarget<Bitmap>(){
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                    image = resource
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//                    // 수명 주기 호출에서 imageView가 지워졌을 때 호출됩니다.
//                    // 이 imageView가 아닌 다른 곳에서 비트맵을 참조하는 경우
//                    // 더 이상 비트맵을 가질 수 없으므로 여기에서 지우십시오
//                }
//            }
//        )


        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE
            else PendingIntent.FLAG_UPDATE_CURRENT
        )

        val style = if(image_url.isEmpty()){
           NotificationCompat.BigTextStyle().setBigContentTitle(title)
                .bigText(body)
        }else{
            NotificationCompat.BigPictureStyle().setBigContentTitle(title)
                .setSummaryText(body)
                .bigPicture(image)
        }

        /* 알림 빌더 */
        val notificationBuilder = NotificationCompat.Builder(this,CHANNEL_ID)
            .setSmallIcon(icon)
            .setStyle(style)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        //Oreo(26) 이상 버전에는 channel 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            channel.description = CHANNEL_DESCRIPTION
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID,notificationBuilder.build())
    }
}