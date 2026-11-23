package com.example.libraryapp.di

import android.app.Application
import com.example.libraryapp.profile.di.ProfileContainer
import com.example.libraryapp.profile.di.ProfileContainerProvider

class LibraryApplication : Application(), ProfileContainerProvider {
    val container by lazy { AppContainer(this) }

    override val profileContainer: ProfileContainer
        get() = container.profileContainer
}