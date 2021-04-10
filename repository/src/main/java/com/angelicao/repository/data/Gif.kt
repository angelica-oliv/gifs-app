package com.angelicao.repository.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Gif(
    val id: String,
    val url: String,
    val largerGifUrl: String,
    val title: String,
    var favorite: Boolean = false
) : Parcelable
