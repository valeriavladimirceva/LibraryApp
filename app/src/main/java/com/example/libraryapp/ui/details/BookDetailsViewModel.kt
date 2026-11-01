package com.example.libraryapp.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.example.libraryapp.di.LibraryApplication
import com.example.libraryapp.domain.model.Book
import com.example.libraryapp.domain.model.DomainError
import com.example.libraryapp.domain.repository.FavoritesRepository
import com.example.libraryapp.domain.usecase.GetBookDetailsUseCase
import com.example.libraryapp.ui.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookDetailsViewModel (
    savedStateHandle: SavedStateHandle,
    private val getBookDetails: GetBookDetailsUseCase,
    private val favorites: FavoritesRepository
): ViewModel() {
    private val bookId: String = savedStateHandle
        .get<String>(Screen.BookDetails.ARG_BOOK_ID)
        .orEmpty()
    private val _uiState = MutableStateFlow<BookDetailsUiState>(BookDetailsUiState.Loading)
    val uiState: StateFlow<BookDetailsUiState> = _uiState.asStateFlow()

    val isFavorite: StateFlow<Boolean> = favorites.isFavorite(bookId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    private var loadedBook: Book? = null

    private fun show(book: Book) {
        loadedBook = book
        _uiState.value = BookDetailsUiState.Success(book.toDetailsUi())
    }

    init {
        loadBook()
    }

    fun loadBook() {
        _uiState.value = BookDetailsUiState.Loading
        viewModelScope.launch {
            val saved = favorites.getFavorite(bookId)
            if (saved != null) {
                show(saved)
                return@launch
            }
            getBookDetails(bookId)
                .onSuccess { show(it) }
                .onFailure { e ->
                    _uiState.value = if (e is DomainError.NotFound) {
                        BookDetailsUiState.NotFound
                    } else {
                        BookDetailsUiState.Error(e as? DomainError ?: DomainError.Unknown)
                    }
                }
        }
    }

    fun onToggleFavorite() {
        val book = loadedBook ?: return
        viewModelScope.launch {
            if (isFavorite.value) favorites.remove(book.id) else favorites.add(book)
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LibraryApplication
                BookDetailsViewModel(
                    createSavedStateHandle(),
                    app.container.getBookDetails,
                    app.container.favoritesRepository
                )
            }
        }
    }
}