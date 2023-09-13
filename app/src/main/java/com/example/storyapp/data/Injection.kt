package com.example.storyapp.data

import android.content.Context
import com.example.storyapp.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): ListRepository {
        val database = ListDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return ListRepository(database, apiService)
    }
}