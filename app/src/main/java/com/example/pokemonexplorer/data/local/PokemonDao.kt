package com.example.pokemonexplorer.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Upsert
    suspend fun upsertAll(pokemons: List<PokemonEntity>)

    @Upsert
    suspend fun upsert(pokemon: PokemonEntity)

    @Query("SELECT * FROM pokemon_table WHERE type = :type")
    fun getPokemonsByType(type: String): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon_table WHERE name LIKE '%' || :query || '%' AND type = :type")
    fun searchPokemons(query: String, type: String): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon_table WHERE name = :name")
    suspend fun getPokemonByName(name: String): PokemonEntity?
}