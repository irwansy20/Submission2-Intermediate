package com.example.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.storyapp.response.ListStoryItem
import com.example.storyapp.retrofit.ApiService

class ListRepository(private val listDatabase: ListDatabase, private val apiService: ApiService) {
    @OptIn(ExperimentalPagingApi::class)
    fun getStories(token: String): LiveData<PagingData<ListStoryItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(listDatabase, apiService, token),
            pagingSourceFactory = {
                listDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    suspend fun getLoc(token: String): List<ListStoryItem>{
        return apiService.getLoc("Bearer $token").listStory
    }
}