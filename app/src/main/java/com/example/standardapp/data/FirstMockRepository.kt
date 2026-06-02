package com.example.standardapp.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirstMockRepository(private val context: Context) {
    suspend fun loadItems(): List<FirstItem> = withContext(Dispatchers.IO) {
        // JSON nam trong assets nen co the doi noi dung ma khong can tao resource id.
        val input = context.assets.open("mock_data.json")
        val text = input.bufferedReader().use { it.readText() }
        val listType = object : TypeToken<List<FirstItemDto>>() {}.type
        val dtoList: List<FirstItemDto> = Gson().fromJson(text, listType)
        dtoList.map { it.toDomain() }
    }
}
