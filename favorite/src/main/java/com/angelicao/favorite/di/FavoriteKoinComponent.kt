package com.angelicao.favorite.di

import org.koin.core.Koin
import org.koin.core.component.KoinComponent

interface FavoriteKoinComponent : KoinComponent {
    override fun getKoin(): Koin = favoriteKoinApplication.koin
}
