package com.example.galleryme.data.datasource.api.entity

import com.example.galleryme.data.datasource.local.entity.ImageDBEntity
import com.example.galleryme.domain.model.Image
import com.google.gson.annotations.SerializedName

data class ImageApiEntity(
    @SerializedName("id")
    val id: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("download_url")
    val downloadURL: String
)

fun ImageApiEntity.toImage(): Image {
    return Image(
        id = id,
        author = author,
        width = width,
        height = height,
        url = url,
        downloadURL = downloadURL
    )
}

fun ImageApiEntity.toImageDBEntity(): ImageDBEntity {
    return ImageDBEntity(
        id = id,
        author = author,
        width = width,
        height = height,
        url = url,
        downloadUrl = downloadURL
    )
}
