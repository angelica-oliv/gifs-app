package com.angelicao.localdata

import android.content.Context
import androidx.room.Room

class GifDatabaseBuilder(private val applicationContext: Context) {
    fun build(): GifDatabase =
        Room.databaseBuilder(
            applicationContext,
            GifDatabase::class.java, "gif-database"
        ).build()
}