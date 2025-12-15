package com.example.pokemonexplorer.presentation.viewmodel

import com.example.pokemonexplorer.domain.model.Pokemon

data class PokemonState(
    val pokemons: List<Pokemon> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedType: String = "fire", // Default type
    val searchQuery: String = ""       // Default search query
)