package com.example.pokemonexplorer.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_table")
data class PokemonEntity(
    @PrimaryKey val name: String,
    val url: String,
    val type: String,
    val imageUrl: String? = null,
    val hp: Int? = null,
    val attack: Int? = null,
    val defense: Int? = null
)