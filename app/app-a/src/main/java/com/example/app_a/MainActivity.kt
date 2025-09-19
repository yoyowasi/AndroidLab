package com.example.app_a

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonCallB: Button = findViewById(R.id.button_call_b)
        buttonCallB.setOnClickListener {
            // 암시적 인텐트 생성
            val intent = Intent()
            // 고유한 액션 설정
            intent.action = "com.example.ACTION_VIEW"
            startActivity(intent)
        }
    }
}