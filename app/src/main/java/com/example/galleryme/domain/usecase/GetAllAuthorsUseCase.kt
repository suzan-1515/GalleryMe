package com.example.galleryme.domain.usecase

import com.example.galleryme.core.PageQuery
import com.example.galleryme.core.UseCase
import com.example.galleryme.core.UseCaseWithParams
import com.example.galleryme.core.helper.Resource
import com.example.galleryme.domain.model.Image
import com.example.galleryme.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get all authors
 * @param repository ImageRepository
 */
class GetAllAuthorsUseCase @Inject constructor(private val repository: ImageRepository) :
    UseCase<Flow<List<String>>> {
    override suspend fun execute(): Flow<List<String>> {
        return repository.getAuthors()
    }
}