package com.example.libraryapp.profile.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import com.example.libraryapp.profile.domain.model.Profile
import com.example.libraryapp.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.profileDataStore: DataStore<ProfileDto> by dataStore(
    fileName = "profile.json",
    serializer = ProfileSerializer,
    corruptionHandler = ReplaceFileCorruptionHandler { ProfileDto() }
)

class DataStoreProfileRepository(context: Context) : ProfileRepository {

    private val dataStore = context.applicationContext.profileDataStore

    override val profile: Flow<Profile> = dataStore.data
        .catch { e ->
            if (e is IOException) emit(ProfileDto()) else throw e
        }
        .map { it.toDomain() }

    override suspend fun save(profile: Profile) {
        dataStore.updateData { profile.toDto() }
    }
}