package com.example.libraryapp.ui.details

import com.example.libraryapp.data.model.Book

sealed interface BookDetailsUiState {
    data object Loading : BookDetailsUiState
    data class Success(val book: Book) : BookDetailsUiState
    data object NotFound : BookDetailsUiState
}