package com.angelicao.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelicao.repository.GifRepository
import com.angelicao.repository.data.Gif
import kotlinx.coroutines.launch

class FavoriteGifListViewModel(private val gifRepository: GifRepository) : ViewModel() {
    private val _gifList = MutableLiveData<List<Gif>>()
    val gifList: LiveData<List<Gif>>
        get() = _gifList

    init {
        viewModelScope.launch {
            _gifList.postValue(gifRepository.getFavoriteGif())
        }
    }

    fun removeFavorite(gif: Gif) {
        viewModelScope.launch {
            gifRepository.removeFavoriteGif(gif)
            removeItemFromGifList(gif)
        }
    }

    private fun removeItemFromGifList(gif: Gif) {
        val mutableGifList = _gifList.value?.toMutableList()
        mutableGifList?.remove(gif)
        _gifList.postValue(mutableGifList)
    }
}
