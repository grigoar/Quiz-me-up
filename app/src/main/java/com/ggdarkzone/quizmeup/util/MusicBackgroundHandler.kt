package com.ggdarkzone.quizmeup.util

import android.content.Context
import android.media.MediaPlayer
import com.ggdarkzone.quizmeup.R


object MusicBackgroundHandler {

    var mediaPlayer: MediaPlayer? = null
    var lastResource: Int? = null


    //play a soundfile
    fun playSoundFile(context: Context, fileName: Int?, loop: Boolean, appIsHavingSound: Boolean) {
        if (appIsHavingSound == false) {
            return
        }
        if (mediaPlayer == null) {
            if (fileName == null) {
                mediaPlayer = MediaPlayer.create(context, R.raw.bg1)
                mediaPlayer?.setVolume(0.7f, 0.7f)
                mediaPlayer?.isLooping = loop
                mediaPlayer?.start()
            } else {
                mediaPlayer = MediaPlayer.create(context, fileName)
                mediaPlayer?.start()
            }
        } else if (mediaPlayer?.isPlaying == false) {
            lastResource?.let { mediaPlayer?.seekTo(it) }
            mediaPlayer?.start()
        } else {
        }


    }
    fun stopSound() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun pauseSound(){
        mediaPlayer?.pause()
        lastResource = mediaPlayer?.currentPosition

    }
}