package com.angelicao.localdata

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.angelicao.localdata.data.FavoriteGif

@Dao
interface GifDao {
    @Query("SELECT * FROM favorite_gif")
    fun getAll(): List<FavoriteGif>

    @Insert
    fun insertAll(vararg favoriteGif: FavoriteGif)

    @Delete
    fun delete(favoriteGif: FavoriteGif)
}
