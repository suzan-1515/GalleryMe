package com.example.galleryme.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val downloadURL: String
) : Parcelable
