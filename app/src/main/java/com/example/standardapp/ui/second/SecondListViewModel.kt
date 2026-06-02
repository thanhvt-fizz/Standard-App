package com.example.standardapp.ui.second

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.standardapp.data.SecondListViewUiState
import com.example.standardapp.data.SecondMockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SecondListViewModel @Inject constructor(
    private val repository: SecondMockRepository
) : ViewModel() {
    private val _state = MutableStateFlow<SecondListViewUiState>(SecondListViewUiState.Loading)
    val state: StateFlow<SecondListViewUiState> = _state

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            try {
                val repositories = repository.loadRepositories()
                _state.value = SecondListViewUiState.Success(repositories)
            } catch (e: Exception) {
                _state.value = SecondListViewUiState.Error(e.localizedMessage ?: "Unknown")
            }
        }
    }
}
