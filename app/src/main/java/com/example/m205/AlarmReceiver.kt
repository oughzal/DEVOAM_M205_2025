package com.example.m205

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer

class AlarmReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action != "ALARM_ACTION") return

        playRingtone(context)
    }

    private fun playRingtone(context: Context?) {
        val mediaplayer = MediaPlayer.create(context, R.raw.alarm2)
        mediaplayer.start()
    }
}