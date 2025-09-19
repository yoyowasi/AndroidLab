package com.example.ch15_threadtest

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        var thread = Thread(WorkerRunble())
        thread.start()

        var cnt = 0
        while (cnt <100){
            cnt += 1
            Log.d("WorkerRunble", "cnt:$cnt")
        }
    }
}
class WorkerRunble: Thread(){
    override fun run(){
        var i = 0
        while (i<100){
            i += 1
            Log.d("WorkerRunble","i:$i")
        }
    }
}