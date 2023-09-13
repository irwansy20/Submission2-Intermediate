package com.example.storyapp.stories

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.storyapp.data.Injection
import com.example.storyapp.data.ListRepository
import java.lang.IllegalArgumentException

class ListStoryViewModel(private val listRepository: ListRepository): ViewModel() {

    fun getStory(token: String) = listRepository.getStories(token).cachedIn(viewModelScope)

}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListStoryViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ListStoryViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}