package com.example.galleryme.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.galleryme.data.datasource.local.dao.ImageDaoService
import com.example.galleryme.data.datasource.local.entity.ImageDBEntity

@Database(entities = [ImageDBEntity::class], version = 1, exportSchema = false)
abstract class ImageDB : RoomDatabase() {
    abstract fun imageDao(): ImageDaoService
}