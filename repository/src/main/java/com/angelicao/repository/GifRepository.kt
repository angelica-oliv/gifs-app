package com.angelicao.repository

import com.angelicao.localdata.GifDao
import com.angelicao.localdata.data.FavoriteGif
import com.angelicao.repository.data.Gif
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GifRepository(private val gifDao: GifDao, private val coroutineDispatcher: CoroutineDispatcher) {

    suspend fun favoriteGif(gif: Gif) {
        withContext(coroutineDispatcher) {
            val favoriteGif = gif.run {
                FavoriteGif(
                    id = id,
                    url = url,
                    largerGifUrl = largerGifUrl,
                    title = title
                )
            }
            gifDao.insertAll(favoriteGif)
        }
    }

    suspend fun removeFavoriteGif(gif: Gif) {
        withContext(coroutineDispatcher) {
            val favoriteGif = gif.run {
                FavoriteGif(
                    id = id,
                    url = url,
                    largerGifUrl = largerGifUrl,
                    title = title
                )
            }
            gifDao.delete(favoriteGif)
        }
    }

    suspend fun getFavoriteGif(): List<Gif> =
        withContext(coroutineDispatcher) {
            gifDao.getAll().map { favoriteGif ->
                Gif(
                    id = favoriteGif.id,
                    url = favoriteGif.url,
                    largerGifUrl = favoriteGif.largerGifUrl,
                    title = favoriteGif.title,
                    favorite = true
                )
            }
        }
}
