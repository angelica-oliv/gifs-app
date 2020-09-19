package com.angelicao.network

import com.angelicao.network.service.GifAPI

class GifRemoteDataSource(private val api: GifAPI) {
    suspend fun getGIFs(offset: Int = 0) = api.getTrendingGIFs(offset = offset)
}