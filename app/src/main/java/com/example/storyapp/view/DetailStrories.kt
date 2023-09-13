package com.example.storyapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityDetailStroriesBinding
import com.example.storyapp.response.ListStoryItem
import com.example.storyapp.stories.StoryAdapter

class DetailStrories : AppCompatActivity() {

    private lateinit var detailBinding: ActivityDetailStroriesBinding
    private lateinit var detailStories: ListStoryItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailStroriesBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setLogo(R.drawable.ic_back)
        supportActionBar?.setDisplayUseLogoEnabled(true)

        detailStories = intent.getParcelableExtra<ListStoryItem>(StoryAdapter.DETAIL_STORIES) as ListStoryItem
        supportActionBar?.title = detailStories.name

        setData()
    }

    private fun setData() {
        detailBinding.apply {
            Glide.with(this@DetailStrories)
                .load(detailStories.photoUrl)
                .into(detailBinding.imgPhoto)
            tvName.text = detailStories.name
            tvDescContent.text = detailStories.description
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return super.onSupportNavigateUp()
    }
}