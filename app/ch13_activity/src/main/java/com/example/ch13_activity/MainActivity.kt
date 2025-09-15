package com.example.ch13_activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ch13_activity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var datas: MutableList<String>? = null
    lateinit var adapter: MyAdapter // MyAdapter는 launcher를 받을 수 있도록 수정 필요

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // AddActivity 결과를 처리하는 Launcher (원본 코드의 requestLauncher 이름 유지)
        val requestLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result -> // 명확성을 위해 'it' 대신 'result' 사용
            if (result.resultCode == Activity.RESULT_OK) { // 결과 코드 확인
                result.data?.getStringExtra("result")?.let {
                    datas?.add(it)
                    datas?.let { ds ->
                        adapter.notifyItemInserted(ds.size - 1)
                    }
                    Log.d("MainActivity", "Item added: $it")
                }
            }
        }

        // DeleteActivity 결과를 처리하는 Launcher
        val deleteRequestLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val deletedPosition = result.data?.getIntExtra("deleted_item_position", -1) ?: -1
                if (deletedPosition != -1 && datas != null && deletedPosition < datas!!.size) {
                    datas?.removeAt(deletedPosition)
                    adapter.notifyItemRemoved(deletedPosition)
                    Log.d("MainActivity", "Item deleted at position: $deletedPosition")
                } else {
                    Log.w("MainActivity", "Failed to delete item or invalid position: $deletedPosition")
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                Log.d("MainActivity", "Deletion cancelled by user.")
            }
        }

        binding.mainFab.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            requestLauncher.launch(intent) // *** 여기를 requestLauncher로 사용 (원본 이름) ***
        }

        datas = savedInstanceState?.let {
            it.getStringArrayList("datas")?.toMutableList()
        } ?: mutableListOf<String>()


        val layoutManager = LinearLayoutManager(this)
        binding.mainRecyclerView.layoutManager = layoutManager
        // MyAdapter 초기화 시, deleteRequestLauncher를 전달
        adapter = MyAdapter(datas, deleteRequestLauncher) // *** 여기에 deleteRequestLauncher 전달 ***
        binding.mainRecyclerView.adapter = adapter
        binding.mainRecyclerView.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        datas?.let {
            outState.putStringArrayList("datas", ArrayList(it))
        }
    }
}