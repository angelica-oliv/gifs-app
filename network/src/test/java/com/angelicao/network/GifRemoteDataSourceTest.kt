package com.angelicao.network

import com.angelicao.network.data.GifData
import com.angelicao.network.data.GifImage
import com.angelicao.network.data.GifImageData
import com.angelicao.network.data.GifResponse
import com.angelicao.network.service.GifAPI
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

private val GIF_RESPONSE = GifResponse(listOf(GifData("gif", "123", "title", GifImage(GifImageData("http://giphy.com")))))
@ExperimentalCoroutinesApi
class GifRemoteDataSourceTest {
    private val gifAPI = mockk<GifAPI>()
    private val gifRemoteDataSource = GifRemoteDataSource(gifAPI)

    @Test
    fun whenGifIsRequested_gifFromApiIsReturned() = runBlockingTest {
        coEvery { gifAPI.getTrendingGIFs() } returns GIF_RESPONSE

        val valueReturned = gifRemoteDataSource.getGIFs()

        coVerify { gifAPI.getTrendingGIFs(offset = 0) }
        assertEquals(GIF_RESPONSE, valueReturned)
    }

    @Test
    fun whenSecondPageGifIsRequested_gifFromApiIsReturnedAndSecondPageIsCalled() = runBlockingTest {
        coEvery { gifAPI.getTrendingGIFs(offset = 2) } returns GIF_RESPONSE

        val valueReturned = gifRemoteDataSource.getGIFs(2)

        coVerify { gifAPI.getTrendingGIFs(offset = 2) }
        assertEquals(GIF_RESPONSE, valueReturned)
    }
}
