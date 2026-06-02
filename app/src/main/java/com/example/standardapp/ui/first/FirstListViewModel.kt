package com.example.standardapp.ui.first

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.standardapp.data.FirstMockRepository
import com.example.standardapp.data.FirstListViewUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirstListViewModel @Inject constructor(
    private val repository: FirstMockRepository
) : ViewModel() {
    private val _state = MutableStateFlow<FirstListViewUiState>(FirstListViewUiState.Loading)
    val state: StateFlow<FirstListViewUiState> = _state

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            try {
                val repositories = repository.loadItems()
                _state.value = FirstListViewUiState.Success(repositories)
            } catch (e: Exception) {
                _state.value = FirstListViewUiState.Error(e.localizedMessage ?: "Unknown")
            }
        }
    }
}
