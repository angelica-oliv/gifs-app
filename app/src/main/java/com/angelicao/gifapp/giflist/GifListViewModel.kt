package com.angelicao.gifapp.giflist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelicao.repository.GifRepository
import com.angelicao.repository.data.Gif
import kotlinx.coroutines.launch

class GifListViewModel(private val gifRepository: GifRepository): ViewModel() {
    private val _gifList = MutableLiveData<List<Gif>>()
    val gifList: LiveData<List<Gif>>
        get() = _gifList

    init {
        viewModelScope.launch {
            _gifList.postValue(gifRepository.getGIFs())
        }
    }
}