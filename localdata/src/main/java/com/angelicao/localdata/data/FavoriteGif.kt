package com.angelicao.localdata.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_gif")
data class FavoriteGif(
    @PrimaryKey val id: String,
    val url: String,
    val largerGifUrl: String,
    val title: String
)
