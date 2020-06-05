package com.angelicao.network

import androidx.paging.PositionalDataSource
import com.angelicao.network.data.GifData
import com.angelicao.network.service.GifAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GifRemoteDataSource(private val api: GifAPI): PositionalDataSource<GifData>() {
    suspend fun getGIFs() = api.getTrendingGIFs()
    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<GifData>) {
        GlobalScope.launch(Dispatchers.IO) {
            callback.onResult(api.getTrendingGIFs().data)
        }
    }

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<GifData>
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            callback.onResult(api.getTrendingGIFs().data, params.requestedStartPosition)
        }
    }
}