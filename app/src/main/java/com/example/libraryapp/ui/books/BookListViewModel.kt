package com.example.libraryapp.ui.books

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.data.mock.MockBooks
import com.example.libraryapp.data.model.Book
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookListUiState(
    val items: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val endReached: Boolean = false
)

class BookListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(BookListUiState(isLoading = true))

    val uiState: StateFlow<BookListUiState> = _uiState.asStateFlow()

    private val pageSize = 5
    private var nextPage = 0

    init {
        loadNextPage(initial = true)
    }

    fun loadNextPage(initial: Boolean = false) {
        val current = _uiState.value
        if (current.isLoadingMore || current.endReached) return
        if (!initial && current.isLoading) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = initial,
                    isLoadingMore = !initial
                )
            }

            delay(600)

            val all = MockBooks.books
            val from = nextPage * pageSize
            val to = (from + pageSize).coerceAtMost(all.size)
            val chunk = if (from < all.size) all.subList(from, to) else emptyList()
            nextPage++
            _uiState.update { state ->
                state.copy(
                    items = state.items + chunk,
                    isLoading = false,
                    isLoadingMore = false,
                    endReached = nextPage * pageSize >= all.size
                )
            }
        }

    }

}