package com.angelicao.repository

import com.angelicao.localdata.GifDao
import com.angelicao.localdata.data.FavoriteGif
import com.angelicao.network.GifRemoteDataSource
import com.angelicao.network.data.GifData
import com.angelicao.network.data.GifImage
import com.angelicao.network.data.GifImageData
import com.angelicao.network.data.GifResponse
import com.angelicao.repository.data.Gif
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

private val GIF_REMOTE = GifResponse(listOf(GifData("gif", "123", "title",  GifImage(
    GifImageData("http://giphy.com")
)
)))
private val GIF_DATABASE = listOf(FavoriteGif("123","http://giphy.com", "title"))
private val GIF_REPOSITORY_LIST = listOf(Gif("123","http://giphy.com", "title"))
private val GIF_REPOSITORY_FAVORITE_LIST = listOf(Gif("123","http://giphy.com", "title", true))
class GifRepositoryTest {
    private val gifRemoteDataSource = mockk<GifRemoteDataSource>()
    private val gifDao = mockk<GifDao>(relaxed = true)
    private val gifRepository = GifRepository(gifRemoteDataSource, gifDao)

    @Test
    fun whenRemoteGifDataIsRequested_gifIsReturned() = runBlocking {
        coEvery { gifRemoteDataSource.getGIFs() } returns GIF_REMOTE

        val valueReturned = gifRepository.getGIFs()

        assertEquals(GIF_REPOSITORY_LIST.size, valueReturned.size)
        GIF_REPOSITORY_LIST.first().let { gif ->
            assertEquals(gif.id, valueReturned.first().id)
            assertEquals(gif.url, valueReturned.first().url)
            assertEquals(gif.title, valueReturned.first().title)
            assertEquals(gif.favorite, valueReturned.first().favorite)
        }
    }

    @Test
    fun whenThereIsFavoriteGifOnDatabase_favoriteGifIsReturned() = runBlocking {
        coEvery { gifRemoteDataSource.getGIFs() } returns GIF_REMOTE
        every { gifDao.getAll() } returns GIF_DATABASE

        val valueReturned = gifRepository.getGIFs()

        assertEquals(GIF_REPOSITORY_LIST.size, valueReturned.size)
        GIF_REPOSITORY_FAVORITE_LIST.first().let { gif ->
            assertEquals(gif.id, valueReturned.first().id)
            assertEquals(gif.url, valueReturned.first().url)
            assertEquals(gif.title, valueReturned.first().title)
            assertEquals(gif.favorite, valueReturned.first().favorite)
        }
    }
}
