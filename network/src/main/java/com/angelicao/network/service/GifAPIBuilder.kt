package com.angelicao.network.service

import com.angelicao.network.BuildConfig
import com.angelicao.network.BuildConfig.GIPHY_URL
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class GifAPIBuilder {
    private fun getOkHttpClient(): OkHttpClient {
        return if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        } else {
            OkHttpClient.Builder()
                .build()
        }
    }

    fun build(): GifAPI {
        return Retrofit.Builder()
            .baseUrl(GIPHY_URL)
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
            .client(getOkHttpClient())
            .build()
            .create(GifAPI::class.java)
    }
}
