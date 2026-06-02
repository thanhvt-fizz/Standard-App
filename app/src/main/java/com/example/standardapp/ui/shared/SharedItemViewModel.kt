package com.example.standardapp.ui.shared

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class DetailContent(
    val title: String,
    val description: String,
    val metadata: String
)

@HiltViewModel
class SharedItemViewModel @Inject constructor() : ViewModel() {
    private val _selectedDetail = MutableStateFlow<DetailContent?>(null)
    val selectedDetail: StateFlow<DetailContent?> = _selectedDetail

    fun selectDetail(content: DetailContent) {
        _selectedDetail.value = content
    }
}
