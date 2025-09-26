package com.example.staticbroadcast

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.staticbroadcast.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // activity_main.xml에 있는 버튼 ID를 사용합니다.
        // 버튼 ID가 sendStaticBroadcastButton이 아니라면 이 부분을 수정해주세요.
        binding.sendStaticBroadcastButton.setOnClickListener {

            // MyReceiver 클래스를 직접 지정해서 인텐트를 만듭니다.
            val intent = Intent(this, MyReceiver::class.java)

            // 브로드캐스트를 보냅니다.
            sendBroadcast(intent)
        }
    }
}