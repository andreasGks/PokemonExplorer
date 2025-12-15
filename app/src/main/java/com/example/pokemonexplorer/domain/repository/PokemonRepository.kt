package com.example.pokemonexplorer.domain.repository

import com.example.pokemonexplorer.domain.model.Pokemon
import com.example.pokemonexplorer.util.Resource
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    // Επιστρέφει ένα Flow που εκπέμπει Loading, Success ή Error
    fun getPokemons(fetchFromRemote: Boolean, type: String): Flow<Resource<List<Pokemon>>>

    suspend fun getPokemonInfo(name: String): Resource<Pokemon>
}