package com.example.libraryapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.libraryapp.domain.model.Profile
import com.example.libraryapp.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.profileDataStore by preferencesDataStore(name = "profile")

class DataStoreProfileRepository(context: Context) : ProfileRepository {

    private val dataStore = context.applicationContext.profileDataStore

    private object Keys {
        val FULL_NAME = stringPreferencesKey("full_name")
        val PHOTO_URI = stringPreferencesKey("photo_uri")
        val RESUME_URL = stringPreferencesKey("resume_url")
    }

    override val profile: Flow<Profile> = dataStore.data
        .catch { e ->
            if (e is IOException) emit(emptyPreferences()) else throw e
        }
        .map { prefs ->
            Profile(
                fullName = prefs[Keys.FULL_NAME].orEmpty(),
                photoUri = prefs[Keys.PHOTO_URI],
                resumeUrl = prefs[Keys.RESUME_URL].orEmpty()
            )
        }

    override suspend fun save(profile: Profile) {
        dataStore.edit { prefs ->
            prefs[Keys.FULL_NAME] = profile.fullName
            prefs[Keys.RESUME_URL] = profile.resumeUrl
            val photo = profile.photoUri
            if (photo != null) prefs[Keys.PHOTO_URI] = photo else prefs.remove(Keys.PHOTO_URI)
        }
    }
}