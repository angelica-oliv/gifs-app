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

    fun onFavoriteClicked(gif: Gif) {
        viewModelScope.launch {
            gif.run {
                if (favorite) {
                    gifRepository.removeFavoriteGif(this)
                } else {
                    gifRepository.favoriteGif(this)
                }
            }
            updateGifList(gif)
        }
    }

    private fun updateGifList(gif: Gif) {
        val mutableGifList = _gifList.value?.toMutableList()
        mutableGifList?.indexOf(gif)?.let { index ->
            mutableGifList.set(index, gif.apply { favorite = favorite.not() })
        }
        _gifList.postValue(mutableGifList)
    }
}