package com.example.ch13_activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch13_activity.databinding.ActivityDeleteBinding

class DeleteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.delete) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        binding.btnConfirm.setOnClickListener {
            val position = intent.getIntExtra("item_position", -1)
            val resultIntent = Intent()
            resultIntent.putExtra("deleted_item_position", position)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
