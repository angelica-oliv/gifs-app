package com.angelicao.gifapp.giflist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.angelicao.repository.GifRepository
import com.angelicao.repository.data.Gif
import com.angelicao.repository.source.GifPagingSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private val FAVORITE_GIF = Gif(id = "1",
    url = "test.com",
    largerGifUrl = "test.com",
    title = "test",
    favorite = true)
private val GIF = Gif(id = "1",
    url = "test.com",
    largerGifUrl = "test.com",
    title = "test",
    favorite = false)

@ExperimentalCoroutinesApi
class GifListViewModelTest {
    @JvmField @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val repository = mockk<GifRepository>(relaxed = true)
    private val gifPagingSource = mockk<GifPagingSource>()
    private lateinit var gifViewModel: GifListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockLoadGifList()

        gifViewModel = GifListViewModel(repository, gifPagingSource)
    }

    @Test
    fun onFavoriteClicked_whenItemIsFavorite_favoriteIsRemovedFromRepository() = runBlockingTest {
        gifViewModel.onFavoriteClicked(FAVORITE_GIF)

        coVerify { repository.removeFavoriteGif(FAVORITE_GIF) }
    }

    @Test
    fun onFavoriteClicked_whenItemIsNotFavorite_favoriteIsAddedInRepository() = runBlockingTest {
        gifViewModel.onFavoriteClicked(GIF)

        coVerify { repository.favoriteGif(GIF) }
    }

    private fun mockLoadGifList() {
        coEvery { gifPagingSource.load(any()) } returns mockk(relaxed = true)
    }
}