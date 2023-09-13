package com.example.storyapp.maps

import android.content.Context
import androidx.lifecycle.*
import com.example.storyapp.data.Injection
import com.example.storyapp.data.ListRepository
import com.example.storyapp.response.ListStoryItem
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MapsVieModel(private val listRepository: ListRepository): ViewModel() {
    private val _listLoc = MutableLiveData<List<ListStoryItem>>()
    var lastLoc: LiveData<List<ListStoryItem>> = _listLoc

    fun getLoc(token: String){
        viewModelScope.launch {
            _listLoc.postValue(listRepository.getLoc(token))
        }
    }
}

class MapsViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsVieModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return MapsVieModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown View Model Class: ")
    }
}