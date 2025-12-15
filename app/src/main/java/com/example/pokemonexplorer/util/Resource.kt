package com.example.pokemonexplorer.util

// Μια σφραγισμένη κλάση που κρατάει δεδομένα (data) και μήνυμα λάθους (message)
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(val isLoading: Boolean = true) : Resource<T>(null)
}