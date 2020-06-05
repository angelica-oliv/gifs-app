package com.angelicao.network

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.angelicao.network.data.GifData
import org.koin.core.KoinComponent
import org.koin.core.get

class GifRemoteDataSourceFactory: DataSource.Factory<Int, GifData>(), KoinComponent {
    val sourceLiveData = MutableLiveData<GifRemoteDataSource>()
    var latestSource: GifRemoteDataSource? = null

    override fun create(): DataSource<Int, GifData>? {
        latestSource = GifRemoteDataSource(get())
        sourceLiveData.postValue(latestSource)
        return latestSource
    }
}