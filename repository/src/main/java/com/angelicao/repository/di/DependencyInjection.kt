package com.angelicao.repository.di

import com.angelicao.localdata.GifDatabaseBuilder
import com.angelicao.network.GifRemoteDataSource
import com.angelicao.network.service.GifAPIBuilder
import com.angelicao.repository.GifRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single { GifAPIBuilder().build() }
    single { GifRemoteDataSource(get()) }
    single { GifDatabaseBuilder(androidContext()).build().gifDao() }
    factory { GifRepository(get(), get()) }
}