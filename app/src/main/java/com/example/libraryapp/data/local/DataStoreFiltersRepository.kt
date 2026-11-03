package com.example.libraryapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.libraryapp.domain.model.SearchFilters
import com.example.libraryapp.domain.model.SortOrder
import com.example.libraryapp.domain.repository.FiltersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.filtersDataStore by preferencesDataStore(name = "search_filters")

class DataStoreFiltersRepository(context: Context) : FiltersRepository {
    private val dataStore = context.applicationContext.filtersDataStore

    private object Keys {
        val GENRE = stringPreferencesKey("genre")
        val SORT = stringPreferencesKey("sort")
    }

    override val filters: Flow<SearchFilters> = dataStore.data
        .catch { e ->
            if (e is IOException) emit(emptyPreferences()) else throw e
        }
        .map { prefs ->
            SearchFilters(
                genre = prefs[Keys.GENRE],
                sort = SortOrder.entries.firstOrNull { it.name == prefs[Keys.SORT] }
                    ?: SortOrder.RELEVANCE
            )
        }

    override suspend fun saveFilters(filters: SearchFilters) {
        dataStore.edit { prefs ->
            prefs.setOrRemove(Keys.GENRE, filters.genre)
            prefs[Keys.SORT] = filters.sort.name
        }
    }

    private fun MutablePreferences.setOrRemove(key: Preferences.Key<String>, value: String?) {
        if (value != null) this[key] = value else remove(key)
    }
}