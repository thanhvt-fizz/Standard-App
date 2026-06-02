package com.example.standardapp.data

import com.google.gson.annotations.SerializedName

data class FirstItemDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("type")
    val type: String
) {
    fun toDomain(): FirstItem {
        return FirstItem(
            id = id,
            title = title,
            description = description,
            type = ItemType.valueOf(type)
        )
    }
}
