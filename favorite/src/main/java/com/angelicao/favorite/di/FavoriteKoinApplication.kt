package com.angelicao.favorite.di

import com.angelicao.favorite.FavoriteGifListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.KoinContextHandler
import org.koin.dsl.koinApplication
import org.koin.dsl.module

val favoriteKoinApplication = koinApplication {
    modules(
        module {
            viewModel { FavoriteGifListViewModel(KoinContextHandler.get().get()) }
        }
    )
}
