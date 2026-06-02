package com.example.standardapp.data

data class SecondItem(
    val id: String,
    val name: String,
    val owner: String,
    val status: String,
    val stars: Int,
    val summary: String
)

sealed class SecondListViewUiState {
    object Loading : SecondListViewUiState()
    data class Success(val repositories: List<SecondItem>) : SecondListViewUiState()
    data class Error(val message: String) : SecondListViewUiState()
}
