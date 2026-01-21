package com.angelicao.favorite.di

import com.angelicao.favorite.FavoriteGifListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext
import org.koin.dsl.koinApplication
import org.koin.dsl.module

val favoriteKoinApplication = koinApplication {
    modules(
        module {
            viewModel { FavoriteGifListViewModel(GlobalContext.get().get()) }
        }
    )
}
