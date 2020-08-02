package com.angelicao.gifapp.giflist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.angelicao.repository.GifRepository
import com.angelicao.repository.data.Gif
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private val FAVORITE_GIF = Gif(id = "1",
    url = "test.com",
    title = "test",
    favorite = true)
private val GIF = Gif(id = "1",
    url = "test.com",
    title = "test",
    favorite = false)
private val GIF_LIST = listOf(FAVORITE_GIF, GIF)

@ExperimentalCoroutinesApi
class GifListViewModelTest {
    @JvmField @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val repository = mockk<GifRepository>(relaxed = true)
    private lateinit var gifViewModel: GifListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockRepository()

        gifViewModel = GifListViewModel(repository)
    }

    @Test
    fun onFavoriteClicked_whenItemIsFavorite_favoriteIsRemoved() = runBlockingTest {
        gifViewModel.onFavoriteClicked(FAVORITE_GIF)

        coVerify { repository.removeFavoriteGif(FAVORITE_GIF) }
        val favoriteAfter = gifViewModel.gifList.value?.first()?.favorite ?: true
        assertFalse(favoriteAfter)
    }

    @Test
    fun onFavoriteClicked_whenItemIsNotFavorite_favoriteIsAdded() = runBlockingTest {
        gifViewModel.onFavoriteClicked(GIF)

        coVerify { repository.favoriteGif(GIF) }
        val favoriteAfter = gifViewModel.gifList.value?.first()?.favorite ?: true
        assertTrue(favoriteAfter)
    }

    private fun mockRepository() {
        coEvery { repository.getGIFs() } returns GIF_LIST
    }
}