package com.angelicao.network

import androidx.paging.PositionalDataSource
import com.angelicao.network.data.GifData
import com.angelicao.network.service.GifAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GifRemoteDataSource(private val api: GifAPI): PositionalDataSource<GifData>() {
    suspend fun getGIFs() = api.getTrendingGIFs()
    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<GifData>) {
        TODO("Not yet implemented")
    }

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<GifData>
    ) {
        launch(Dispatchers.IO) {
            callback.onResult(api.getTrendingGIFs().data, 0)
        }
    }
}