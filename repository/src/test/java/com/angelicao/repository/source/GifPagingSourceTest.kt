package com.angelicao.repository.source

import androidx.paging.PagingSource
import com.angelicao.network.GifRemoteDataSource
import com.angelicao.network.data.GifData
import com.angelicao.network.data.GifImage
import com.angelicao.network.data.GifImageData
import com.angelicao.network.data.GifResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

private val GIF_RESPONSE = GifResponse(listOf(GifData("gif", "123", "title", GifImage(GifImageData("http://giphy.com")))))
@ExperimentalCoroutinesApi
class GifPagingSourceTest {
    private val gifRemoteDataSource: GifRemoteDataSource = mockk()
    private val loadParams: PagingSource.LoadParams<Int> = mockk()
    private lateinit var gifPagingSource: GifPagingSource

    @Before
    fun setup() {
        gifPagingSource = GifPagingSource(gifRemoteDataSource)
    }

    @Test
    fun whenNoKeyIsSet_firstPageIsLoaded() = runBlockingTest {
        every { loadParams.key } returns null
        coEvery { gifRemoteDataSource.getGIFs(0) } returns GIF_RESPONSE

        val loadResult: PagingSource.LoadResult.Page<Int, GifData> =
            gifPagingSource.load(loadParams) as PagingSource.LoadResult.Page<Int, GifData>

        coVerify { gifRemoteDataSource.getGIFs(0) }
        assertEquals(GIF_RESPONSE.data, loadResult.data)
        assertEquals(null, loadResult.prevKey)
        assertEquals(1, loadResult.nextKey)
    }

    @Test
    fun whenKeyIsSet_pageOfKeyIsLoaded() = runBlockingTest {
        every { loadParams.key } returns 10
        coEvery { gifRemoteDataSource.getGIFs(10) } returns GIF_RESPONSE

        val loadResult: PagingSource.LoadResult.Page<Int, GifData> =
            gifPagingSource.load(loadParams) as PagingSource.LoadResult.Page<Int, GifData>

        coVerify { gifRemoteDataSource.getGIFs(10) }
        assertEquals(GIF_RESPONSE.data, loadResult.data)
        assertEquals(9, loadResult.prevKey)
        assertEquals(11, loadResult.nextKey)
    }

    @Test
    fun whenApiThrowException_errorIsReturned() = runBlockingTest {
        every { loadParams.key } returns 10
        val exception = IOException()
        coEvery { gifRemoteDataSource.getGIFs(10) } throws exception

        val loadError: PagingSource.LoadResult.Error<Int, GifData> =
            gifPagingSource.load(loadParams) as PagingSource.LoadResult.Error<Int, GifData>

        coVerify { gifRemoteDataSource.getGIFs(10) }
        assertEquals(exception, loadError.throwable)
    }
}