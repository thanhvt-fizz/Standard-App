package com.example.standardapp.data

data class FirstItem(
    val id: String,
    val title: String,
    val description: String,
    val type: ItemType
)

enum class ItemType { TYPE_A, TYPE_B }

sealed class FirstListViewUiState {
    object Loading : FirstListViewUiState()
    data class Success(val items: List<FirstItem>) : FirstListViewUiState()
    data class Error(val message: String) : FirstListViewUiState()
}
