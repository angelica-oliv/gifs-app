package com.angelicao.repository

import com.angelicao.network.GifRemoteDataSource
import com.angelicao.network.data.GifResponse
import com.angelicao.repository.data.Gif

class GifRepository(private val gifRemoteDataSource: GifRemoteDataSource) {
    suspend fun getGIFs(): List<Gif> {
        val apiData = gifRemoteDataSource.getGIFs()

        return getGifFromApiData(apiData)
    }

    private fun getGifFromApiData(apiData: GifResponse): List<Gif> =
        apiData.data.map { gifData ->
            Gif(url = gifData.embedUrl)
        }
}