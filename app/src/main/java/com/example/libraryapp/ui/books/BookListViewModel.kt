package com.example.libraryapp.ui.books

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.example.libraryapp.di.LibraryApplication
import com.example.libraryapp.domain.model.Book
import com.example.libraryapp.domain.model.DomainError
import com.example.libraryapp.domain.usecase.SearchBooksUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookListUiState(
    val query: String = "",
    val items: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val endReached: Boolean = false,
    val error: DomainError? = null
)

class BookListViewModel(private val searchBooks: SearchBooksUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(BookListUiState(isLoading = true))

    val uiState: StateFlow<BookListUiState> = _uiState.asStateFlow()

    private var nextPage = 1
    private var loadJob: Job? = null

    init {
        loadNextPage(initial = true)
    }

    fun onQueryChange(newQuery: String) {
        loadJob?.cancel()
        nextPage = 1
        _uiState.value = BookListUiState(query = newQuery, isLoading = true)
        loadNextPage(initial = true, debounceMs = DEBOUNCE_MS)
    }

    fun loadNextPage(initial: Boolean = false, debounceMs: Long = 0) {
        val current = _uiState.value
        if (current.isLoadingMore || current.endReached || current.error != null) return
        if (!initial && current.isLoading) return

        loadJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = initial,
                    isLoadingMore = !initial
                )
            }
            if (debounceMs > 0) delay(debounceMs)
            searchBooks(_uiState.value.query, nextPage)
                .onSuccess { page ->
                    nextPage++
                    _uiState.update { state ->
                        val items = (state.items + page.books).distinctBy { it.id }
                        state.copy(
                            items = items,
                            isLoading = false,
                            isLoadingMore = false,
                            endReached = page.books.isEmpty() || items.size >= page.total
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            error = e as? DomainError ?: DomainError.Unknown
                        )
                    }
                }
        }
    }

    fun retry() {
        val initial = _uiState.value.items.isEmpty()
        _uiState.update { it.copy(error = null) }
        loadNextPage(initial)
    }

    companion object {
        private const val DEBOUNCE_MS = 500L
        val Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LibraryApplication
                BookListViewModel(app.container.searchBooks)
            }
        }
    }

}