package com.example.galleryme.core.helper

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferenceHelper @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun getAuthor(): String {
        return sharedPreferences.getString("author", "")?:""
    }

    fun setAuthor(author: String) {
        sharedPreferences.apply {
            edit().putString("author", author).apply()
        }
    }
}