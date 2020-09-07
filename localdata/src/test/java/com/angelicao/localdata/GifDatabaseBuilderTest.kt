package com.angelicao.localdata

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class GifDatabaseBuilderTest {

    private lateinit var gifDatabaseBuilder: GifDatabaseBuilder
    private val context: Context = mockk()

    @Before
    fun setup() {
        gifDatabaseBuilder = GifDatabaseBuilder(context)
    }

    @Test
    fun build_databaseBuilderIsCalled() {
        val gifDatabaseMock = mockk<GifDatabase>()
        mockkStatic(Room::class)
        every { Room.databaseBuilder(context, GifDatabase::class.java, "gif-database").build() } returns gifDatabaseMock

        gifDatabaseBuilder.build()

        verify(exactly = 1) { Room.databaseBuilder(context, GifDatabase::class.java, "gif-database") }
    }
}