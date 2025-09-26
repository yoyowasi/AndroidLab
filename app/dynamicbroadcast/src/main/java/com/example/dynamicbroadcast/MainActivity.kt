package com.example.dynamicbroadcast

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat // ContextCompat 임포트 추가
import com.example.dynamicbroadcast.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val dynamicReceiver = MyReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendDynamicBroadcastButton.setOnClickListener {
            val intent = Intent("com.example.MY_DYNAMIC_ACTION")
            intent.setPackage(this.packageName)
            sendBroadcast(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter("com.example.MY_DYNAMIC_ACTION")

        // 1. 최신 안드로이드 보안 규칙에 맞춰 리시버를 등록합니다.
        ContextCompat.registerReceiver(this, dynamicReceiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED)
    }

    override fun onPause() {
        super.onPause()
        // 2. 앱이 비활성화되면 리시버를 해제합니다.
        unregisterReceiver(dynamicReceiver)
    }
}