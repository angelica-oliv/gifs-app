package com.angelicao.network

import com.angelicao.network.data.GifData
import com.angelicao.network.data.GifResponse
import com.angelicao.network.service.GifAPI
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

import org.junit.Assert.*

private val GIF_RESPONSE = GifResponse(listOf(GifData("gif", "123", "http://giphy.com")))
class GifRemoteDataSourceTest {
    private val gifAPI = mockk<GifAPI>()
    private val gifRemoteDataSource = GifRemoteDataSource(gifAPI)

    @Test
    fun whenGifIsRequested_gifFromApiIsReturned() = runBlockingTest {
        coEvery { gifAPI.getTrendingGIFs() } returns GIF_RESPONSE

        val valueReturned = gifRemoteDataSource.getGIFs()

        assertEquals(GIF_RESPONSE, valueReturned)
    }
}
