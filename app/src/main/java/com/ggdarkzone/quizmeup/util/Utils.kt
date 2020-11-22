package com.ggdarkzone.quizmeup.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.TypedValue
import android.view.View
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

class Utils {
companion object{
    fun decodeFromBase64toString(stringToDecode: String?): String {
        val textEncoded: ByteArray? =
            android.util.Base64.decode(stringToDecode, android.util.Base64.DEFAULT)
        val textDecoded: String = String(textEncoded!!)
        return textDecoded
    }
    fun dipToPixels(context: Context, dipValue: Float): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics)
    }

    fun gradientForBackground(context: Context, color1:String, color2:String): GradientDrawable {
        var gradientColors: IntArray = IntArray(2)
        gradientColors.set(0, Color.parseColor(color1))
        gradientColors.set(1, Color.parseColor(color2))

        var gradientDrowable: GradientDrawable = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,gradientColors)

        val strokeWidth = dipToPixels(context,40f)
        gradientDrowable.cornerRadius=strokeWidth

        gradientDrowable.setStroke(5, Color.parseColor("#ffffff"))
        return gradientDrowable
    }

    fun gradientForButtonsBackground(context: Context, color1:String, color2:String): GradientDrawable {
        var gradientColors: IntArray = IntArray(2)
        gradientColors.set(0, Color.parseColor(color1))
        gradientColors.set(1, Color.parseColor(color2))

        var gradientDrowable: GradientDrawable = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,gradientColors)

        val strokeWidth = dipToPixels(context,5f)
        gradientDrowable.cornerRadius=strokeWidth

        gradientDrowable.setStroke(5, Color.parseColor("#ffffff"))
        return gradientDrowable
    }


    fun createSoundPoolObject():SoundPool{

        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
            .build()
        val soundPool = SoundPool.Builder()
            .setMaxStreams(4)
            .setAudioAttributes(audioAttributes)
            .build()
        return soundPool
    }

    fun createSoundPoolObjectTickTack():SoundPool{
        var soundPool:SoundPool
        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()
        return soundPool
    }
}

}