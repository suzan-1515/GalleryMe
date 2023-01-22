package com.example.galleryme.core.di

import android.content.Context
import androidx.room.Room
import com.example.galleryme.data.datasource.local.ImageDB
import com.example.galleryme.data.datasource.local.dao.ImageDaoService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): ImageDB {
        return Room.databaseBuilder(
            context,
            ImageDB::class.java,
            "image_db.db"
        ).build()
    }

    @Provides
    fun provideImageDao(imageDatabase: ImageDB): ImageDaoService {
        return imageDatabase.imageDao()
    }
}