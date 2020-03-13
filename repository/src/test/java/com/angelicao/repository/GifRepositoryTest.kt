package com.angelicao.repository

import com.angelicao.network.GifRemoteDataSource
import com.angelicao.network.data.GifData
import com.angelicao.network.data.GifImage
import com.angelicao.network.data.GifImageData
import com.angelicao.network.data.GifResponse
import com.angelicao.repository.data.Gif
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

import org.junit.Assert.*

private val GIF_REMOTE = GifResponse(listOf(GifData("gif", "123", "title",  GifImage(
    GifImageData("http://giphy.com")
)
)))
private val GIF_REPOSITORY_LIST = listOf(Gif("http://giphy.com", "title"))
class GifRepositoryTest {
    private val gifRemoteDataSource = mockk<GifRemoteDataSource>()
    private val gifRepository = GifRepository(gifRemoteDataSource)

    @Test
    fun whenRemoteGifDataIsRequested_gifIsReturned() = runBlockingTest {
        coEvery { gifRemoteDataSource.getGIFs() } returns GIF_REMOTE

        val valueReturned = gifRepository.getGIFs()

        assertEquals(GIF_REPOSITORY_LIST.size, valueReturned.size)
        GIF_REPOSITORY_LIST.first().let { gif ->
            assertEquals(gif.url, valueReturned.first().url)
            assertEquals(gif.title, valueReturned.first().title)
            assertEquals(gif.favorite, valueReturned.first().favorite)
        }

    }
}
