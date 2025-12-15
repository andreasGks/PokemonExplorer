package com.example.pokemonexplorer.domain.model

data class Pokemon(
    val name: String,
    val url: String,
    val imageUrl: String?,
    val hp: Int?,
    val attack: Int?,
    val defense: Int?,
    val type: String
)