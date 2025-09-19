// MainActivity.kt
package com.example.ch14_recevier

import android.content.BroadcastReceiver
import android.content.Context
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

// MainActivity 클래스는 앱의 주 화면을 담당하며, AppCompatActivity를 상속받습니다.
class MainActivity : AppCompatActivity() {

    // 뷰 바인딩을 위한 변수 선언. activity_main.xml 레이아웃의 뷰들에 접근할 수 있게 해줍니다.
    private lateinit var binding: ActivityMainBinding

    // 화면 켜짐/꺼짐 이벤트를 수신하기 위한 MyReceiver 인스턴스 생성
    private val screenReceiver = MyReceiver()

    // 배터리 상태 변경 이벤트를 수신하기 위한 BroadcastReceiver 변수 선언
    // null로 초기화하여 나중에 객체를 할당합니다.
    private var batteryReceiver: BroadcastReceiver? = null

    // 액티비티가 생성될 때 호출되는 메소드
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰 바인딩 객체 초기화
        binding = ActivityMainBinding.inflate(layoutInflater)
        // 액티비티의 화면을 activity_main.xml 레이아웃으로 설정
        setContentView(binding.root)

        // 화면 켜짐/꺼짐 이벤트를 수신하기 위한 인텐트 필터 설정
        val screenFilter = IntentFilter(Intent.ACTION_SCREEN_ON).apply {
            // ACTION_SCREEN_OFF 액션도 추가하여 화면이 꺼질 때도 감지하도록 설정
            addAction(Intent.ACTION_SCREEN_OFF)
        }
        // screenReceiver를 시스템에 등록하여 위에서 설정한 액션들을 수신하도록 합니다. (동적 등록)
        registerReceiver(screenReceiver, screenFilter)
        // 배터리 상태 변경을 감지하는 BroadcastReceiver 객체 생성 및 구현
        batteryReceiver = object : BroadcastReceiver() {
            // 브로드캐스트를 수신했을 때 호출되는 메소드
            override fun onReceive(context: Context, intent: Intent) {
                // 인텐트에서 배터리 상태 정보를 가져옵니다. 기본값은 -1입니다.
                when (intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)) {
                    // 배터리가 충전 중인 경우
                    BatteryManager.BATTERY_STATUS_CHARGING -> {
                        // 충전 방식을 확인합니다.
                        when (intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)) {
                            // USB로 충전 중인 경우
                            BatteryManager.BATTERY_PLUGGED_USB -> {
                                binding.chargingResultView.text = "USB Plugged"
                                // usb.jpg 이미지를 화면에 표시합니다.
                                binding.chargingImageView.setImageBitmap(
                                    BitmapFactory.decodeResource(resources, R.drawable.usb)
                                )
                            }
                            // AC 어댑터로 충전 중인 경우
                            BatteryManager.BATTERY_PLUGGED_AC -> {
                                binding.chargingResultView.text = "AC Plugged"
                                // ac.jpg 이미지를 화면에 표시합니다.
                                binding.chargingImageView.setImageBitmap(
                                    BitmapFactory.decodeResource(resources, R.drawable.ac)
                                )
                            }
                            // 그 외의 충전 방식인 경우
                            else -> {
                                binding.chargingResultView.text = "Charging"
                            }
                        }
                    }
                    // 배터리가 충전 중이 아닌 경우
                    else -> {
                        binding.chargingResultView.text = "Not Plugged"
                    }
                }
                // 현재 배터리 잔량을 가져옵니다.
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                // 배터리 최대 용량을 가져옵니다.
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                // 배터리 잔량과 최대 용량 정보가 유효한 경우
                if (level >= 0 && scale > 0) {
                    // 백분율 계산
                    val batteryPct = level / scale.toFloat() * 100
                    binding.percentResultView.text = "${batteryPct} %"
                } else {
                    // 정보를 가져올 수 없는 경우 "N/A"로 표시
                    binding.percentResultView.text = "N/A"
                }
            }
        }

        // 배터리 상태 변경을 감지하기 위한 인텐트 필터 생성
        val batteryFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        // 위에서 생성한 batteryReceiver를 시스템에 등록합니다. (동적 등록)
        registerReceiver(batteryReceiver, batteryFilter)

        // 권한 요청 결과를 처리하기 위한 런처 등록
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            // 요청한 모든 권한이 승인되었는지 확인
            if (it.all { permission -> permission.value }) {
                // MyReceiver로 명시적 브로드캐스트를 보냅니다.
                val intent = Intent(this, MyReceiver::class.java)
                sendBroadcast(intent) // 이 브로드캐스트는 MyReceiver에서 알림을 표시하게 합니다.
            } else {
                // 권한이 하나라도 거부된 경우 토스트 메시지 표시
                Toast.makeText(this, "permission denied...", Toast.LENGTH_LONG).show()
            }
        }

        // 레이아웃의 버튼에 클릭 리스너 설정
        binding.button.setOnClickListener {
            // 안드로이드 13 (TIRAMISU) 버전 이상인지 확인
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // 알림 표시 권한이 이미 승인되었는지 확인
                if (ContextCompat.checkSelfPermission(
                        this,
                        "android.permission.POST_NOTIFICATIONS"
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    // 권한이 있으면 바로 MyReceiver로 브로드캐스트를 보냅니다.
                    val intent = Intent(this, MyReceiver::class.java)
                    sendBroadcast(intent)
                } else {
                    // 권한이 없으면 permissionLauncher를 통해 권한을 요청합니다.
                    permissionLauncher.launch(
                        arrayOf("android.permission.POST_NOTIFICATIONS")
                    )
                }
            } else {
                // 안드로이드 13 미만 버전에서는 권한 없이 바로 브로드캐스트를 보냅니다.
                val intent = Intent(this, MyReceiver::class.java)
                sendBroadcast(intent)
            }
        }
    }

    // 액티비티가 소멸될 때 호출되는 메소드
    override fun onDestroy() {
        super.onDestroy()
        // 앱이 종료될 때 동적으로 등록한 리시버들을 해제하여 메모리 누수를 방지합니다.
        try {
            unregisterReceiver(screenReceiver)
            unregisterReceiver(batteryReceiver)
        } catch (_: IllegalArgumentException) {
            // 리시버가 이미 해제된 상태에서 또 해제하려고 할 때 발생하는 예외를 처리합니다.
            // 특별한 작업 없이 넘어갑니다.
        }
    }
}