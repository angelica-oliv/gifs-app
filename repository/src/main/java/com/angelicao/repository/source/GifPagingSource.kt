package com.angelicao.repository.source

import androidx.paging.PagingSource
import com.angelicao.localdata.GifDao
import com.angelicao.network.GifRemoteDataSource
import com.angelicao.network.data.GifResponse
import com.angelicao.repository.data.Gif

class GifPagingSource(
    private val gifRemoteDataSource: GifRemoteDataSource,
    private val gifDao: GifDao
): PagingSource<Int, Gif>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Gif> {
        return try {
            val offset = params.key ?: 0
            val response = gifRemoteDataSource.getGIFs(offset)

            LoadResult.Page(
                data = getGifFromApiData(response),
                prevKey = if (offset == 0) null else offset - response.data.size,
                nextKey = offset + response.data.size
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private fun getGifFromApiData(apiData: GifResponse): List<Gif> {
        val favoriteIDs = gifDao.getAll().map {
            it.id
        }

        return apiData.data
            .filter { gifData -> gifData.id != null }
            .map { gifData ->
                Gif(
                    id = gifData.id ?: "",
                    url = gifData.images?.preview_gif?.url ?: "",
                    title = gifData.title ?: "",
                    favorite = favoriteIDs.contains(gifData.id)
                )
            }
    }

}