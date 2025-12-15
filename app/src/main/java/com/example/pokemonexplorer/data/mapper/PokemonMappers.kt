package com.example.pokemonexplorer.data.mapper

import com.example.pokemonexplorer.data.local.PokemonEntity
import com.example.pokemonexplorer.data.remote.dto.PokemonSummary
import com.example.pokemonexplorer.domain.model.Pokemon

fun PokemonEntity.toDomain(): Pokemon {
    return Pokemon(
        name = name,
        url = url,
        imageUrl = imageUrl,
        hp = hp,
        attack = attack,
        defense = defense,
        type = type
    )
}

fun PokemonSummary.toEntity(type: String): PokemonEntity {
    // 1. Βγάζουμε το ID από το URL (π.χ. "https://pokeapi.co/api/v2/pokemon/25/")
    val id = url.trimEnd('/').split("/").last()

    // 2. Φτιάχνουμε το URL της επίσημης εικόνας μόνοι μας!
    // Αυτό μας γλιτώνει ένα network call για την εικόνα.
    val officialArtworkUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

    return PokemonEntity(
        name = name,
        url = url,
        type = type,
        imageUrl = officialArtworkUrl, // <--- ΤΩΡΑ ΕΧΟΥΜΕ ΕΙΚΟΝΑ ΑΜΕΣΩΣ!
        hp = null,
        attack = null,
        defense = null
    )
}