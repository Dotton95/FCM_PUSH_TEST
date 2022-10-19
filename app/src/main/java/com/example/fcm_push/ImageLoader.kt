package com.example.fcm_push

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

object ImageLoader {
    suspend fun loadImage(input_url:String): Bitmap?{
        val bitmap:Bitmap?=null
        try{
            val url = URL(input_url)
            val stream = url.openStream()
            return BitmapFactory.decodeStream(stream)
        }catch (e:MalformedURLException){ //잘못된 URL이 원인 URL 수정 필요
            e.printStackTrace()
        }catch (e:IOException){
            e.printStackTrace()
        }
        return bitmap
    }
}