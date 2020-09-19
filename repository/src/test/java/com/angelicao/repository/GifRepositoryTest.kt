package com.angelicao.repository

import com.angelicao.localdata.GifDao
import com.angelicao.localdata.data.FavoriteGif
import com.angelicao.repository.data.Gif
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

private val GIF_DATABASE = listOf(FavoriteGif("123","http://giphy.com", "title"))
private val GIF = Gif("123","http://giphy.com", "title")
private val FAVORITE_GIF = FavoriteGif("123", "http://giphy.com", "title")
private val GIF_REPOSITORY_FAVORITE_LIST = listOf(Gif("123","http://giphy.com", "title", true))
@ExperimentalCoroutinesApi
class GifRepositoryTest {
    private val gifDao = mockk<GifDao>(relaxed = true)
    private val testDispatcher = TestCoroutineDispatcher()
    private val gifRepository = GifRepository(gifDao, testDispatcher)

    @Test
    fun whenAddingFavoriteGif_favoriteGifIsAddedToDatabase() = runBlockingTest {
        gifRepository.favoriteGif(GIF)

        coVerify { gifDao.insertAll(FAVORITE_GIF) }
    }

    @Test
    fun whenRemovingFavoriteGif_favoriteGifIsRemovedFromDatabase() = runBlockingTest {
        gifRepository.removeFavoriteGif(GIF)

        coVerify { gifDao.delete(FAVORITE_GIF) }
    }

    @Test
    fun whenGettingAllFavorites_favoriteGifIsReturnedFromDatabase() = runBlockingTest {
        every { gifDao.getAll() } returns GIF_DATABASE

        val favoriteGifList = gifRepository.getFavoriteGif()

        coVerify { gifDao.getAll() }
        assertEquals(GIF_REPOSITORY_FAVORITE_LIST, favoriteGifList)
    }
}
