package com.example.pokemonexplorer.presentation.viewmodel

sealed class PokemonEvent {
    data class OnTypeChange(val type: String): PokemonEvent()
    data class OnSearchQueryChange(val query: String): PokemonEvent()
    object LoadMore: PokemonEvent() // Το προσθέσαμε για το κουμπί Load More
}