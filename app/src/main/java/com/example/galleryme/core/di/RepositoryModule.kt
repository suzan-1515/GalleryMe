package com.example.galleryme.core.di

import com.example.galleryme.data.datasource.api.service.ImageApiService
import com.example.galleryme.data.datasource.local.dao.ImageDaoService
import com.example.galleryme.data.repository.ImageRepositoryImpl
import com.example.galleryme.domain.repository.ImageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun providesImageRepository(
        imageApiService: ImageApiService,
        imageDaoService: ImageDaoService,
    ): ImageRepository {
        return ImageRepositoryImpl(imageApiService, imageDaoService, Dispatchers.IO)
    }

}