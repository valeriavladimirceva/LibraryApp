package com.example.libraryapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.libraryapp.di.LibraryApplication
import com.example.libraryapp.domain.cache.FiltersBadgeCache
import com.example.libraryapp.domain.model.SearchFilters
import com.example.libraryapp.domain.model.SortOrder
import com.example.libraryapp.domain.repository.FiltersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val draft: SearchFilters = SearchFilters(),
    val isLoaded: Boolean = false
)

class SettingsViewModel(
    private val repository: FiltersRepository,
    private val badgeCache: FiltersBadgeCache
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val saved = repository.filters.first()
            _uiState.value = SettingsUiState(draft = saved, isLoaded = true)
        }
    }

    fun onGenreSelected(genre: String?) =
        _uiState.update { it.copy(draft = it.draft.copy(genre = genre)) }

    fun onSortSelected(sort: SortOrder) =
        _uiState.update { it.copy(draft = it.draft.copy(sort = sort)) }

    fun onReset(onSaved: () -> Unit) {
        val defaults = SearchFilters()
        _uiState.update { it.copy(draft = defaults) }
        viewModelScope.launch {
            repository.saveFilters(defaults)
            badgeCache.update(defaults)
            onSaved()
        }
    }

    fun onDone(onSaved: () -> Unit) {
        viewModelScope.launch {
            repository.saveFilters(_uiState.value.draft)
            badgeCache.update(_uiState.value.draft)
            onSaved()
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LibraryApplication
                SettingsViewModel(
                    app.container.filtersRepository,
                    app.container.filtersBadgeCache)
            }
        }
    }
}