package com.angelicao.network.service

import com.angelicao.network.data.GifResponse
import retrofit2.http.GET

interface GifAPI {
    // TODO pagination API when getting
    @GET("trending")
    suspend fun getTrendingGIFs(): GifResponse
}