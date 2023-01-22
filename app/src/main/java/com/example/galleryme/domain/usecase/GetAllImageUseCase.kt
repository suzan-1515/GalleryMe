package com.example.galleryme.domain.usecase

import com.example.galleryme.core.PageQuery
import com.example.galleryme.core.UseCaseWithParams
import com.example.galleryme.core.helper.Resource
import com.example.galleryme.domain.model.Image
import com.example.galleryme.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get all images
 * @param repository ImageRepository
 */
class GetAllImageUseCase @Inject constructor(private val repository: ImageRepository) :
    UseCaseWithParams<Flow<Resource<List<Image>>>, GetAllImagesUseCaseParams> {
    override suspend fun execute(params: GetAllImagesUseCaseParams): Flow<Resource<List<Image>>> {
        return repository.getImages(params.pageQuery)
    }
}

/**
 * Use case params for GetAllImageUseCase
 * @param pageQuery PageQuery
 */
class GetAllImagesUseCaseParams(
    val pageQuery: PageQuery,
)