package com.example.standardapp.ui.first

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.standardapp.data.FirstItem
import com.example.standardapp.data.FirstListViewUiState
import com.example.standardapp.data.FirstMockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FirstListViewModel @Inject constructor(
    private val repository: FirstMockRepository
) : ViewModel() {
    private val _sourceState = MutableStateFlow<FirstListViewUiState>(FirstListViewUiState.Loading)
    private val _searchQuery = MutableStateFlow("")
    val state: StateFlow<FirstListViewUiState> =
        combine(_sourceState, _searchQuery) { state, query ->
            state to query.trim()
        }.transformLatest { (state, query) ->
            when {
                state !is FirstListViewUiState.Success -> emit(state)
                query.isBlank() -> emit(state.copy(searchQuery = ""))
                else -> {
                    emit(FirstListViewUiState.Loading)
                    delay(SEARCH_DEBOUNCE_MS)
                    val filteredItems = withContext(Dispatchers.Default) {
                        state.items.filter { it.matches(query) }
                    }
                    emit(
                        state.copy(
                            items = filteredItems,
                            searchQuery = query
                        )
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FirstListViewUiState.Loading
        )

    init {
        load()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun load() {
        viewModelScope.launch {
            _sourceState.value = FirstListViewUiState.Loading
            try {
                val repositories = repository.loadItems()
                _sourceState.value = FirstListViewUiState.Success(repositories)
            } catch (e: Exception) {
                _sourceState.value = FirstListViewUiState.Error(e.localizedMessage ?: "Unknown")
            }
        }
    }

    private fun FirstItem.matches(query: String): Boolean {
        if (query.isBlank()) return true

        return title.contains(query, ignoreCase = true) ||
            description.contains(query, ignoreCase = true) ||
            id.contains(query, ignoreCase = true) ||
            type.name.contains(query, ignoreCase = true)
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 250L
    }
}
