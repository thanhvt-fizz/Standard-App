package com.example.standardapp.data

import com.google.gson.annotations.SerializedName

data class SecondItemDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("owner")
    val owner: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("stars")
    val stars: Int,
    @SerializedName("summary")
    val summary: String
) {
    fun toDomain(): SecondItem {
        return SecondItem(
            id = id,
            name = name,
            owner = owner,
            status = status,
            stars = stars,
            summary = summary
        )
    }
}
