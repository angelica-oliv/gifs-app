package com.angelicao.gifapp.giflist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.angelicao.repository.GifRepository
import com.angelicao.repository.data.Gif
import kotlinx.coroutines.launch

private const val PAGE_SIZE = 20
class GifListViewModel(private val gifRepository: GifRepository): ViewModel() {
    private var _gifList: LiveData<PagedList<Gif>>? = null
    val gifList: LiveData<PagedList<Gif>>?
        get() = _gifList

    init {
        viewModelScope.launch {
            _gifList = gifRepository.getGIFs(PAGE_SIZE)
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
        val pagedList = gifList?.value
        pagedList?.indexOf(gif)?.let { index ->
            pagedList.set(index, gif.apply { favorite = favorite.not() })
        }

        _gifList = MutableLiveData<PagedList<Gif>>().apply {
            value = pagedList
        }
    }
}