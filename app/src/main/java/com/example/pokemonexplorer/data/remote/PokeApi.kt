package com.example.pokemonexplorer.data.remote

import com.example.pokemonexplorer.data.remote.dto.PokemonDetailDto
import com.example.pokemonexplorer.data.remote.dto.TypeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApi {

    // 1. Φέρε όλα τα Pokemon ενός συγκεκριμένου τύπου (π.χ. fire)
    @GET("type/{type}")
    suspend fun getPokemonsByType(
        @Path("type") type: String
    ): TypeResponse

    // 2. Φέρε λεπτομέρειες για ένα συγκεκριμένο Pokemon με βάση το όνομα
    @GET("pokemon/{name}")
    suspend fun getPokemonInfo(
        @Path("name") name: String
    ): PokemonDetailDto
}