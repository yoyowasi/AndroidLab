package com.example.staticbroadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "정적 브로드캐스트 토스트!", Toast.LENGTH_LONG).show()
    }
}