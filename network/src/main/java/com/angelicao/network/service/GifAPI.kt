package com.angelicao.network.service

import com.angelicao.network.BuildConfig
import com.angelicao.network.data.GifResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GifAPI {
    // TODO pagination API when getting
    @GET("trending")
    suspend fun getTrendingGIFs(@Query("api_key") apiKey: String = BuildConfig.GIPHY_API_KEY,
                                @Query("limit") limit: Int = 25,
                                @Query("offset") offset: Int = 0): GifResponse
}