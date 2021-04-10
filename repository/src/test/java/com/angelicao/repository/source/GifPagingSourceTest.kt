package com.angelicao.repository.source

import androidx.paging.PagingSource
import com.angelicao.localdata.GifDao
import com.angelicao.localdata.data.FavoriteGif
import com.angelicao.network.GifRemoteDataSource
import com.angelicao.network.data.GifData
import com.angelicao.network.data.GifImage
import com.angelicao.network.data.GifImageData
import com.angelicao.network.data.GifResponse
import com.angelicao.repository.data.Gif
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

private val GIF_RESPONSE = GifResponse(listOf(GifData("gif", "123", "title", GifImage(GifImageData("http://giphy.com"), GifImageData("http://giphy.com")))))
private val GIF_DATABASE = listOf(FavoriteGif("123","http://giphy.com", "http://giphy.com","title"))
private val GIF_EXPECTED_RESULT = listOf(Gif("123","http://giphy.com", "http://giphy.com","title"))
@ExperimentalCoroutinesApi
class GifPagingSourceTest {
    private val gifRemoteDataSource: GifRemoteDataSource = mockk()
    private val loadParams: PagingSource.LoadParams<Int> = mockk()
    private val gifDao = mockk<GifDao>(relaxed = true)
    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var gifPagingSource: GifPagingSource

    @Before
    fun setup() {
        gifPagingSource = GifPagingSource(gifRemoteDataSource, gifDao, testDispatcher)
    }

    @Test
    fun whenNoKeyIsSet_firstPageIsLoaded() = runBlockingTest {
        every { loadParams.key } returns null
        coEvery { gifRemoteDataSource.getGIFs(0) } returns GIF_RESPONSE

        val loadResult: PagingSource.LoadResult.Page<Int, Gif> =
            gifPagingSource.load(loadParams) as PagingSource.LoadResult.Page<Int, Gif>

        coVerify { gifRemoteDataSource.getGIFs(0) }
        GIF_EXPECTED_RESULT.first().let { gif ->
            assertEquals(gif.id, loadResult.data.first().id)
            assertEquals(gif.url, loadResult.data.first().url)
            assertEquals(gif.title, loadResult.data.first().title)
            assertEquals(false, loadResult.data.first().favorite)
        }
        assertEquals(null, loadResult.prevKey)
        assertEquals(1, loadResult.nextKey)
    }

    @Test
    fun whenKeyIsSet_pageOfKeyIsLoaded() = runBlockingTest {
        every { loadParams.key } returns 10
        coEvery { gifRemoteDataSource.getGIFs(10) } returns GIF_RESPONSE

        val loadResult: PagingSource.LoadResult.Page<Int, Gif> =
            gifPagingSource.load(loadParams) as PagingSource.LoadResult.Page<Int, Gif>

        coVerify { gifRemoteDataSource.getGIFs(10) }
        GIF_EXPECTED_RESULT.first().let { gif ->
            assertEquals(gif.id, loadResult.data.first().id)
            assertEquals(gif.url, loadResult.data.first().url)
            assertEquals(gif.title, loadResult.data.first().title)
            assertEquals(false, loadResult.data.first().favorite)
        }
        assertEquals(9, loadResult.prevKey)
        assertEquals(11, loadResult.nextKey)
    }

    @Test
    fun whenDatabaseReturnFavoriteGif_listWithFavoriteIsReturned() = runBlockingTest {
        every { loadParams.key } returns 10
        coEvery { gifRemoteDataSource.getGIFs(10) } returns GIF_RESPONSE
        every { gifDao.getAll() } returns GIF_DATABASE

        val loadResult: PagingSource.LoadResult.Page<Int, Gif> =
            gifPagingSource.load(loadParams) as PagingSource.LoadResult.Page<Int, Gif>

        coVerify { gifRemoteDataSource.getGIFs(10) }
        GIF_EXPECTED_RESULT.first().let { gif ->
            assertEquals(gif.id, loadResult.data.first().id)
            assertEquals(gif.url, loadResult.data.first().url)
            assertEquals(gif.title, loadResult.data.first().title)
            assertEquals(true, loadResult.data.first().favorite)
        }
        assertEquals(9, loadResult.prevKey)
        assertEquals(11, loadResult.nextKey)
    }

    @Test
    fun whenApiThrowException_errorIsReturned() = runBlockingTest {
        every { loadParams.key } returns 10
        val exception = IOException()
        coEvery { gifRemoteDataSource.getGIFs(10) } throws exception

        val loadError: PagingSource.LoadResult.Error<Int, Gif> =
            gifPagingSource.load(loadParams) as PagingSource.LoadResult.Error<Int, Gif>

        coVerify { gifRemoteDataSource.getGIFs(10) }
        assertEquals(exception, loadError.throwable)
    }
}