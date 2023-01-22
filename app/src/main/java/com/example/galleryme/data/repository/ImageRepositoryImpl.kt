package com.example.galleryme.data.repository

import com.example.galleryme.core.PageQuery
import com.example.galleryme.core.helper.Resource
import com.example.galleryme.data.datasource.api.entity.toImageDBEntity
import com.example.galleryme.data.datasource.api.service.ImageApiService
import com.example.galleryme.data.datasource.local.dao.ImageDaoService
import com.example.galleryme.data.datasource.local.entity.toImage
import com.example.galleryme.domain.model.Image
import com.example.galleryme.domain.repository.ImageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * @param imageApiService
 * @param imageDaoService
 * @param dispatcher
 */
class ImageRepositoryImpl @Inject constructor(
    private val imageApiService: ImageApiService,
    private val imageDaoService: ImageDaoService,
    private val dispatcher: CoroutineDispatcher
) : ImageRepository {
    override suspend fun getImages(pageQuery: PageQuery): Flow<Resource<List<Image>>> {
        return flow {
            emit(Resource.loading(null))
            val cacheMovies = imageDaoService.loadAll()
            if (cacheMovies.isNotEmpty()) {
                emit(Resource.loading(cacheMovies.map { it.toImage() }))
            }
            try {
                val response = imageApiService.getImages(pageQuery.page, pageQuery.limit)
                if (response.isNotEmpty()) {
                    imageDaoService.insertAll(response.map { it.toImageDBEntity() })
                }
                emit(Resource.success(imageDaoService.loadAll().map { it.toImage() }))
            } catch (e: Exception) {
                emit(Resource.error(e.message.toString(), cacheMovies.map { it.toImage() }))
            }

        }.flowOn(dispatcher)

    }

    override suspend fun getImagesByAuthor(author: String): Flow<List<Image>> {
        return flow {
            emit(imageDaoService.loadAllByAuthor(author).map { it.toImage() })
        }.flowOn(dispatcher)
    }

    override suspend fun getAuthors(): Flow<List<String>> {
        return flow {
            emit(imageDaoService.loadAllAuthors())
        }.flowOn(dispatcher)
    }
}
