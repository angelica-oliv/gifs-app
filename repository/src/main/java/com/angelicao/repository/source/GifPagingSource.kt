package com.angelicao.repository.source

import androidx.paging.PagingSource
import com.angelicao.network.GifRemoteDataSource
import com.angelicao.network.data.GifData

class GifPagingSource(
    private val gifRemoteDataSource: GifRemoteDataSource
): PagingSource<Int, GifData>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GifData> {
        return try {
            val offset = params.key ?: 0
            val response = gifRemoteDataSource.getGIFs(offset)

            LoadResult.Page(
                data = response.data,
                prevKey = if (offset == 0) null else offset - response.data.size,
                nextKey = offset + response.data.size
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}