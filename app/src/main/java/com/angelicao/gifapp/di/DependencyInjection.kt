package com.angelicao.gifapp.di

import com.angelicao.gifapp.giflist.GifListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { GifListViewModel(get(), get()) }
}
