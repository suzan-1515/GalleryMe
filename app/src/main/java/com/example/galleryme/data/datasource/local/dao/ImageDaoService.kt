package com.example.galleryme.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.galleryme.data.datasource.local.entity.ImageDBEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDaoService {
    @Query("SELECT * FROM images")
    fun loadAll(): List<ImageDBEntity>

    @Query("SELECT * FROM images WHERE author = :author")
    fun loadAllByAuthor(author: String): List<ImageDBEntity>

    @Query("SELECT distinct author FROM images")
    fun loadAllAuthors(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(images: List<ImageDBEntity>)

    @Query("DELETE FROM images")
    suspend fun deleteAll()
}