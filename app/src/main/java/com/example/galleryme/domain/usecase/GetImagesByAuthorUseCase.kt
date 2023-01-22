package com.example.galleryme.domain.usecase

import com.example.galleryme.core.UseCaseWithParams
import com.example.galleryme.domain.model.Image
import com.example.galleryme.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get images by author
 * @param repository ImageRepository
 */
class GetImagesByAuthorUseCase @Inject constructor(private val repository: ImageRepository) :
    UseCaseWithParams<Flow<List<Image>>, GetImagesByAuthorUseCaseParams> {
    override suspend fun execute(params: GetImagesByAuthorUseCaseParams): Flow<List<Image>> {
        return repository.getImagesByAuthor(params.author)
    }
}
/**
 * Use case params for GetImagesByAuthorUseCase
 * @param repository ImageRepository
 */
class GetImagesByAuthorUseCaseParams(
    val author: String,
)