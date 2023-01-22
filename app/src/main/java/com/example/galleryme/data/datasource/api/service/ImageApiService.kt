package com.example.galleryme.data.datasource.api.service

import com.example.galleryme.data.datasource.api.entity.ImageApiEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApiService {
    @GET("list")
    suspend fun getImages(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): List<ImageApiEntity>
}