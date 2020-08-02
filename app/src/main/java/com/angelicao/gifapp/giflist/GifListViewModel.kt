package com.angelicao.gifapp.giflist

import androidx.lifecycle.*
import com.angelicao.repository.GifRepository
import com.angelicao.repository.data.Gif
import kotlinx.coroutines.launch

class GifListViewModel(private val gifRepository: GifRepository): ViewModel() {
    private val _gifList = MutableLiveData<List<Gif>>()
    val gifList: LiveData<List<Gif>>
        get() = _gifList

    init {
        viewModelScope.launch {
            updateGifList()
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
            updateGifList()
        }
    }

    private suspend fun updateGifList() {
        _gifList.postValue(gifRepository.getGIFs())
    }
}