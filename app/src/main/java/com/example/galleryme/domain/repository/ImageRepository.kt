package com.example.galleryme.domain.repository

import com.example.galleryme.core.PageQuery
import com.example.galleryme.core.helper.Resource
import com.example.galleryme.domain.model.Image
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    /**
     * Get images from the repository
     * @param pageQuery the page query
     * @return a flow of resource
     */
    suspend fun getImages(pageQuery: PageQuery): Flow<Resource<List<Image>>>
    /**
     * Get images by author from the repository
     * @param author the author
     * @return a flow of resource
     */
    suspend fun getImagesByAuthor(author: String): Flow<List<Image>>
    /**
     * Get authors from the repository
     * @return a flow of resource
     */
    suspend fun getAuthors(): Flow<List<String>>
}
