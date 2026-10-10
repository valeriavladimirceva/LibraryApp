package com.example.libraryapp.domain.model

sealed class DomainError : Exception() {
    data object NoConnection : DomainError()
    data object Server : DomainError()
    data object NotFound : DomainError()
    data object Unknown : DomainError()
}