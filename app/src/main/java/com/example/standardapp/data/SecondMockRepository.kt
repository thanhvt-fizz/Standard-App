package com.example.standardapp.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SecondMockRepository(private val context: Context) {
    suspend fun loadRepositories(): List<SecondItem> = withContext(Dispatchers.IO) {
        val input = context.assets.open("mock_second_data.json")
        val text = input.bufferedReader().use { it.readText() }
        val listType = object : TypeToken<List<SecondItemDto>>() {}.type
        val dtoList: List<SecondItemDto> = Gson().fromJson(text, listType)
        dtoList.map { it.toDomain() }
    }
}
