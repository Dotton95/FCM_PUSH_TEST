package com.example.fcm_push

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
        var mIsReceiverRegistered = false
    }

    private val tvResult: TextView by lazy {
        findViewById(R.id.tv_result)
    }

    private val tvToken: TextView by lazy {
        findViewById(R.id.tv_token)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFirebase()
        //updateResult()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        //updateResult(true)
    }

    private fun initFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                tvToken.text = task.result
                Log.d("dotton",task.result)
            }
        }
    }

//    private fun updateResult(isNewIntent: Boolean = false) {
//        //true -> notification 으로 갱신된 것
//        //false -> 아이콘 클릭으로 앱이 실행된 것
//        tvResult.text = (intent.getStringExtra("notificationType") ?: "앱 런처") + if (isNewIntent) {
//            "(으)로 갱신했습니다."
//        } else {
//            "(으)로 실행했습니다."
//        }
//    }
//    private var receiver: BroadcastReceiver? = null
//
//    private fun startRegisterReceiver() {
//        if (!mIsReceiverRegistered) {
//            if (receiver == null) {
//                receiver = object : BroadcastReceiver() {
//                    override fun onReceive(context: Context?, intent: Intent?) {
//                        //notify_icon.setVisibility(View.VISIBLE)
//                    }
//                }
//            }
//            registerReceiver(receiver, IntentFilter("com.package.notification"))
//            mIsReceiverRegistered = true
//        }
//    }
//
//    private fun finishRegisterReceiver() {
//        if (mIsReceiverRegistered) {
//            unregisterReceiver(receiver)
//            receiver = null
//            mIsReceiverRegistered = false
//        }
//    }
//
//    private fun pauseRegisterReceiver() {
//        if (mIsReceiverRegistered) {
//            mIsReceiverRegistered = false
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        startRegisterReceiver()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        pauseRegisterReceiver()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        finishRegisterReceiver()
//    }
//
//    override fun onStart() {
//        super.onStart()
//        startRegisterReceiver()
//    }
}