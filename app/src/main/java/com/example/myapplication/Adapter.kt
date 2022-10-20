package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.TextviewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
1. SqliteHelper -> Room library
2. AsyncTask -> kotlin coroutine
3. Recyclerview.Adapter -> ListAdapter (DiffUtil)
 */

class Adapter(val application: Application) :
    ListAdapter<Fish, Adapter.ContactViewHolder>(DiffUtil()) {

    private val mainActivity = MainActivity.getInstance()

    // 생성된 뷰 홀더에 값 지정
    inner class ContactViewHolder(private val binding: TextviewBinding, application: Application) :
        RecyclerView.ViewHolder(binding.root) {
        private val fishListDao = AppDatabase.getInstance(application)!!.getFishDao()

        @SuppressLint("SetTextI18n")
        fun bind(fish: Fish) {
            binding.favorite.setOnClickListener {
                mainActivity?.favoriteClick(itemView, layoutPosition)
            }

            CoroutineScope(Dispatchers.IO).launch {
                val background = if (fishListDao.selectFavorite(position+1)) R.drawable.favorite
                else R.drawable.favorite_border
                withContext(Dispatchers.Main) {
                    binding.favorite.setBackgroundResource(background)
                }
            }

            binding.fishname.text = "이름 : ${fish.name}"
            binding.fishprice.text = "가격 : ${fish.Price}원"
            Glide.with(itemView).load(fish.image).into(binding.imgPhoto)
        }

    }

    // 어떤 xml 으로 뷰 홀더를 생성할지 지정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = TextviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding, application)
    }

    // 뷰 홀더에 데이터 바인딩
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}