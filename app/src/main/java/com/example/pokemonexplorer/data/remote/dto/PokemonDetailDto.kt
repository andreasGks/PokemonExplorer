package com.example.pokemonexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

// Το response όταν καλούμε https://pokeapi.co/api/v2/pokemon/charizard
data class PokemonDetailDto(
    val id: Int,
    val name: String,
    val sprites: Sprites,
    val stats: List<StatEntry>
)

data class Sprites(
    @SerializedName("front_default")
    val frontDefault: String?
)

data class StatEntry(
    @SerializedName("base_stat")
    val baseStat: Int,
    val stat: StatInfo
)

data class StatInfo(
    val name: String // π.χ. "hp", "attack", "defense"
)