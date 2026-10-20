package com.example.libraryapp.ui.details

import com.example.libraryapp.domain.model.Book
import com.example.libraryapp.domain.model.DomainError

sealed interface BookDetailsUiState {
    data object Loading : BookDetailsUiState
    data class Success(val book: Book) : BookDetailsUiState
    data object NotFound : BookDetailsUiState
    data class Error(val error: DomainError) : BookDetailsUiState
}