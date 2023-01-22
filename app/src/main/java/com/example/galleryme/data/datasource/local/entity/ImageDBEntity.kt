package com.example.galleryme.data.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.galleryme.domain.model.Image

@Entity(tableName = "images")
data class ImageDBEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "width")
    val width: Int,
    @ColumnInfo(name = "height")
    val height: Int,
    @ColumnInfo(name = "download_url")
    val downloadUrl: String
)

fun ImageDBEntity.toImage(): Image {
    return Image(
        id = id,
        author = author,
        width = width,
        height = height,
        url = url,
        downloadURL = downloadUrl
    )
}