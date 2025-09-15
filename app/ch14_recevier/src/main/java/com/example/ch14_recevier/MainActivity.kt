// MainActivity.kt
package com.example.ch14_recevier

import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.BatteryManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.ch14_recevier.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // 화면 ON/OFF 수신용 리시버 인스턴스
    private val screenReceiver = MyReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 화면 ON/OFF 브로드캐스트 등록
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON).apply {
            addAction(Intent.ACTION_SCREEN_OFF)
        }
        registerReceiver(screenReceiver, filter)

        // 알림 권한 런처
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            if (it.all { permission -> permission.value == true }) {
                val intent = Intent(this, MyReceiver::class.java)
                sendBroadcast(intent) // 명시적 브로드캐스트 → MyReceiver에서 알림 표시
            } else {
                Toast.makeText(this, "permisiion denied...", Toast.LENGTH_LONG).show()
            }
        }

        // 배터리 상태(Sticky) 조회
        registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))!!.apply {
            when (getIntExtra(BatteryManager.EXTRA_STATUS, -1)) {
                BatteryManager.BATTERY_STATUS_CHARGING -> {
                    when (getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)) {
                        BatteryManager.BATTERY_PLUGGED_USB -> {
                            binding.chargingResultView.text = "USB Plugged"
                            binding.chargingImageView.setImageBitmap(
                                BitmapFactory.decodeResource(
                                    resources,
                                    R.drawable.usb
                                )
                            )
                        }
                        BatteryManager.BATTERY_PLUGGED_AC -> {
                            binding.chargingResultView.text = "AC Plugged"
                            binding.chargingImageView.setImageBitmap(
                                BitmapFactory.decodeResource(
                                    resources,
                                    R.drawable.ac
                                )
                            )
                        }
                        else -> {
                            binding.chargingResultView.text = "Charging"
                        }
                    }
                }
                else -> {
                    binding.chargingResultView.text = "Not Plugged"
                }
            }
            val level = getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            if (level >= 0 && scale > 0) {
                val batteryPct = level / scale.toFloat() * 100
                binding.percentResultView.text = "${batteryPct} %"
            } else {
                binding.percentResultView.text = "N/A"
            }
        }

        // 버튼 → 알림 테스트(안드 13+ 권한 필요)
        binding.button.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        "android.permission.POST_NOTIFICATIONS"
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val intent = Intent(this, MyReceiver::class.java)
                    sendBroadcast(intent)
                } else {
                    permissionLauncher.launch(
                        arrayOf("android.permission.POST_NOTIFICATIONS")
                    )
                }
            } else {
                val intent = Intent(this, MyReceiver::class.java)
                sendBroadcast(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 동적 등록 리시버 해제
        try {
            unregisterReceiver(screenReceiver)
        } catch (_: IllegalArgumentException) { }
    }
}
