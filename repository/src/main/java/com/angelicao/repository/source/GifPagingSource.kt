package com.angelicao.repository.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.angelicao.localdata.GifDao
import com.angelicao.network.GifRemoteDataSource
import com.angelicao.network.data.GifResponse
import com.angelicao.repository.data.Gif
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException

class GifPagingSource(
    private val gifRemoteDataSource: GifRemoteDataSource,
    private val gifDao: GifDao,
    private val coroutineDispatcher: CoroutineDispatcher
) : PagingSource<Int, Gif>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Gif> {
        return try {
            val offset = params.key ?: 0
            val response = gifRemoteDataSource.getGIFs(offset)

            LoadResult.Page(
                data = getGifFromApiData(response),
                prevKey = if (offset == 0) null else offset - response.data.size,
                nextKey = offset + response.data.size
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }

    private suspend fun getGifFromApiData(apiData: GifResponse): List<Gif> =
        withContext(coroutineDispatcher) {
            val favoriteIDs = gifDao.getAll().map {
                it.id
            }

            apiData.data
                .filter { gifData -> gifData.id != null }
                .map { gifData ->
                    Gif(
                        id = gifData.id ?: "",
                        url = gifData.images?.previewGif?.url ?: "",
                        title = gifData.title ?: "",
                        favorite = favoriteIDs.contains(gifData.id)
                    )
                }
        }

    override fun getRefreshKey(state: PagingState<Int, Gif>): Int? {
        return state.anchorPosition
    }
}
