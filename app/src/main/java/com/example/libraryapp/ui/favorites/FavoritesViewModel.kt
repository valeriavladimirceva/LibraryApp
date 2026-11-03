package com.example.libraryapp.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.libraryapp.di.LibraryApplication
import com.example.libraryapp.domain.repository.FavoritesRepository
import com.example.libraryapp.ui.books.BookItemUi
import com.example.libraryapp.ui.books.toItemUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FavoritesViewModel(repository: FavoritesRepository) : ViewModel() {

    val books: StateFlow<List<BookItemUi>?> = repository.favorites
        .map {list -> list.map { it.toItemUi() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LibraryApplication
                FavoritesViewModel(app.container.favoritesRepository)
            }
        }
    }
}