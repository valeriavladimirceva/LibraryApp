package com.example.libraryapp.core

sealed class DomainError : Exception() {
    data object NoConnection : DomainError()
    data object Server : DomainError()
    data object NotFound : DomainError()
    data object Unknown : DomainError()
}

fun DomainError.messageRes(): Int = when (this) {
    DomainError.NoConnection -> R.string.error_no_connection
    DomainError.Server -> R.string.error_server
    DomainError.NotFound -> R.string.error_not_found
    DomainError.Unknown -> R.string.error_unknown
}