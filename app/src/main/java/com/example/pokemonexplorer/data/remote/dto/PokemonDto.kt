package com.example.pokemonexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

// Το response όταν καλούμε https://pokeapi.co/api/v2/type/fire
data class TypeResponse(
    @SerializedName("pokemon")
    val pokemonList: List<PokemonWrapper>
)

data class PokemonWrapper(
    @SerializedName("pokemon")
    val pokemon: PokemonSummary
)

data class PokemonSummary(
    val name: String,
    val url: String
)