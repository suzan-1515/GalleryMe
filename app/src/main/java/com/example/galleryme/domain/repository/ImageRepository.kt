package com.example.galleryme.domain.repository

import com.example.galleryme.core.PageQuery
import com.example.galleryme.core.helper.Resource
import com.example.galleryme.domain.model.Image
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    suspend fun getImages(pageQuery: PageQuery): Flow<Resource<List<Image>>>
    suspend fun getImagesByAuthor(author: String): Flow<List<Image>>
    suspend fun getAuthors(): Flow<List<String>>
}
