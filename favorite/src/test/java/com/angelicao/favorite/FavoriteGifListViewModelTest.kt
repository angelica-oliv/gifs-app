package com.angelicao.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.angelicao.repository.GifRepository
import com.angelicao.repository.data.Gif
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private val FAVORITE_GIF = Gif(id = "1",
    url = "test.com",
    largerGifUrl = "test.com",
    title = "test",
    favorite = true)
private val GIF_LIST = listOf(FAVORITE_GIF)

class FavoriteGifListViewModelTest {
    @JvmField @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val repository = mockk<GifRepository>(relaxed = true)
    private lateinit var favoriteGifViewModel: FavoriteGifListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockRepository()

        favoriteGifViewModel = FavoriteGifListViewModel(repository)
    }

    @Test
    fun removeFavorite_whenItemIsFavorite_favoriteIsRemoved() = runBlockingTest {
        favoriteGifViewModel.removeFavorite(FAVORITE_GIF)

        coVerify { repository.removeFavoriteGif(FAVORITE_GIF) }
        val gifList = favoriteGifViewModel.gifList.value ?: throw RuntimeException()
        assertEquals(0, gifList.size)
    }

    private fun mockRepository() {
        coEvery { repository.getFavoriteGif() } returns GIF_LIST
    }
}