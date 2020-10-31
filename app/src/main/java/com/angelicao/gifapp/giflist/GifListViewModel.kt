package com.angelicao.gifapp.giflist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.angelicao.repository.GifRepository
import com.angelicao.repository.data.Gif
import com.angelicao.repository.source.GifPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

private const val PAGE_SIZE = 25
class GifListViewModel(
    private val gifRepository: GifRepository,
    private val gifPagingSource: GifPagingSource
) : ViewModel() {
    val gifList: Flow<PagingData<Gif>> = Pager(
        PagingConfig(pageSize = PAGE_SIZE)) {
        gifPagingSource
    }
        .flow
        .cachedIn(viewModelScope)

    fun onFavoriteClicked(gif: Gif) {
        viewModelScope.launch {
            gif.run {
                if (favorite) {
                    gifRepository.removeFavoriteGif(this)
                } else {
                    gifRepository.favoriteGif(this)
                }
            }
        }
    }
}
