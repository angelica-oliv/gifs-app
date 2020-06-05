package com.angelicao.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.Config
import com.angelicao.localdata.GifDao
import com.angelicao.localdata.data.FavoriteGif
import com.angelicao.network.GifRemoteDataSource
import com.angelicao.network.GifRemoteDataSourceFactory
import com.angelicao.repository.data.Gif
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GifRepository(private val gifRemoteDataSourceFactory: GifRemoteDataSourceFactory, private val gifDao: GifDao) {
    fun getGIFs(pageSize: Int): LiveData<PagedList<Gif>> {
        val favoriteIDs = gifDao.getAll().map {
            it.id
        }

        val factory = gifRemoteDataSourceFactory.map { gifData ->
            Gif(
                id = gifData.id ?: "",
                url = gifData.images?.preview_gif?.url ?: "",
                title = gifData.title ?: "",
                favorite = favoriteIDs.contains(gifData.id)
            )
        }
        return LivePagedListBuilder(factory, Config(pageSize, enablePlaceholders = false)).build()
    }

    suspend fun favoriteGif(gif: Gif) {
        withContext(Dispatchers.IO) {
            val favoriteGif = gif.run { FavoriteGif(id = id, url = url, title = title) }
            gifDao.insertAll(favoriteGif)
        }
    }

    suspend fun removeFavoriteGif(gif: Gif) {
        withContext(Dispatchers.IO) {
            val favoriteGif = gif.run { FavoriteGif(id = id, url = url, title = title) }
            gifDao.delete(favoriteGif)
        }
    }

    suspend fun getFavoriteGif() : List<Gif> =
        withContext(Dispatchers.IO) {
            gifDao.getAll().map { favoriteGif ->
                Gif(
                    id = favoriteGif.id,
                    url = favoriteGif.url,
                    title = favoriteGif.title,
                    favorite = true
                )
            }
        }
}