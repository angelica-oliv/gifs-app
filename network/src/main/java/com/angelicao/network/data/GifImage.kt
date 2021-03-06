package com.angelicao.network.data

import com.squareup.moshi.Json

data class GifImage(
    @Json(name = "preview_gif") val previewGif: GifImageData?,
    @Json(name = "downsized_large") val largerGif: GifImageData?
)
