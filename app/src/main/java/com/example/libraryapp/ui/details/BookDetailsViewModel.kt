package com.example.libraryapp.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.libraryapp.data.mock.MockBooks
import com.example.libraryapp.ui.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BookDetailsViewModel (savedStateHandle: SavedStateHandle): ViewModel() {
    private val bookId: String = savedStateHandle
        .get<String>(Screen.BookDetails.ARG_BOOK_ID)
        .orEmpty()
    private val _uiState = MutableStateFlow<BookDetailsUiState>(BookDetailsUiState.Loading)
    val uiState: StateFlow<BookDetailsUiState> = _uiState.asStateFlow()

    init {
        loadBook()
    }

    private fun loadBook() {
        val book = MockBooks.findById(bookId)
        _uiState.value = if (book == null) {
            BookDetailsUiState.NotFound
        } else {
            BookDetailsUiState.Success(book)
        }
    }
}