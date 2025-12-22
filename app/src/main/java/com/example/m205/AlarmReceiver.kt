package com.example.m205

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == "ACTION_ALARM_RING") {
            playAlarmSound(context)
        }
    }

    fun playAlarmSound(context: Context) {

        val mediaPlayer = MediaPlayer.create(context, R.raw.alarm1)

        mediaPlayer.start()
    }

}
