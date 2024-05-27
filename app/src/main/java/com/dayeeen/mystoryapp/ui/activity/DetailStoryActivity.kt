package com.dayeeen.mystoryapp.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dayeeen.mystoryapp.data.response.ListStoryItem
import com.dayeeen.mystoryapp.databinding.ActivityDetailStoryBinding
import com.dayeeen.mystoryapp.viewmodel.DetailViewModel
import com.dayeeen.mystoryapp.viewmodel.ViewModelFactory

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private val dvm by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyId = intent.getStringExtra("USER_ID")
        if (storyId != null) {
            dvm.fetchDetail(storyId)
        }
        dvm.detail.observe(this) { user ->
            setUserDetails(user)
        }

    }

    private fun setUserDetails(user: ListStoryItem) {
        binding.apply {
            dtUserName.text = user.name
            dtCaption.text = user.description
            Glide.with(this@DetailStoryActivity).load(user.photoUrl).into(dtStoryImage)
        }
    }
}