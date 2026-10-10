package com.example.libraryapp.di

import android.app.Application

class LibraryApplication : Application() {
    val container by lazy { AppContainer() }
}