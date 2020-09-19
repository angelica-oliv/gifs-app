package com.angelicao.repository.di

import com.angelicao.localdata.GifDatabaseBuilder
import com.angelicao.network.GifRemoteDataSource
import com.angelicao.network.service.GifAPIBuilder
import com.angelicao.repository.GifRepository
import com.angelicao.repository.source.GifPagingSource
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single { GifAPIBuilder().build() }
    single { GifRemoteDataSource(get()) }
    single { GifDatabaseBuilder(androidContext()).build().gifDao() }
    factory { GifRepository(get(), Dispatchers.IO) }
    factory { GifPagingSource(get(), get()) }
}