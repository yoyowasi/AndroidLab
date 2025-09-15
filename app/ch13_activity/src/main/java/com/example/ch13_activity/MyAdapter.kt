package com.example.ch13_activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ch13_activity.databinding.ItemRecyclerviewBinding

class MyViewHolder(val binding: ItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root)

class MyAdapter(
    val datas: MutableList<String>?,
    private val deleteLauncher: ActivityResultLauncher<Intent> // *** 이 부분이 deleteLauncher를 받도록 되어 있는지 확인 ***
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        binding.itemData.text = datas!![position]

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DeleteActivity::class.java).apply {
                putExtra("item_position", holder.adapterPosition)
            }
            deleteLauncher.launch(intent) // 전달받은 launcher 사용
        }
    }
}