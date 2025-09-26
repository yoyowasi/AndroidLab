package com.example.dynamicbroadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // 이 리시버가 호출되면 토스트 메시지를 띄웁니다.
        Toast.makeText(context, "동적 리시버가 동작했습니다!", Toast.LENGTH_LONG).show()
    }
}