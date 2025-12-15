package com.example.pokemonexplorer.data.repository

import com.example.pokemonexplorer.data.local.PokemonDatabase
import com.example.pokemonexplorer.data.mapper.toDomain
import com.example.pokemonexplorer.data.mapper.toEntity
import com.example.pokemonexplorer.data.remote.PokeApi
import com.example.pokemonexplorer.domain.model.Pokemon
import com.example.pokemonexplorer.domain.repository.PokemonRepository
import com.example.pokemonexplorer.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepositoryImpl @Inject constructor(
    private val api: PokeApi,
    private val db: PokemonDatabase
) : PokemonRepository {

    private val dao = db.dao

    override fun getPokemons(fetchFromRemote: Boolean, type: String): Flow<Resource<List<Pokemon>>> = flow {
        emit(Resource.Loading(true))

        if (fetchFromRemote) {
            try {
                val remoteList = api.getPokemonsByType(type)

                // Περνάμε το 'type' στο mapper για να αποθηκευτεί σωστά στη βάση
                val entities = remoteList.pokemonList.map { it.pokemon.toEntity(type) }

                dao.upsertAll(entities)

            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data. Check your internet connection."))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Server error."))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error("Unknown error."))
            }
        }

        // Φιλτράρουμε από τη βάση ΜΟΝΟ αυτά που ανήκουν στον συγκεκριμένο τύπο
        dao.getPokemonsByType(type).collect { entities ->
            emit(Resource.Success(entities.map { it.toDomain() }))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getPokemonInfo(name: String): Resource<Pokemon> {
        return try {
            val cachedPokemon = dao.getPokemonByName(name)

            if (cachedPokemon != null && cachedPokemon.hp != null) {
                return Resource.Success(cachedPokemon.toDomain())
            }

            val remoteDetails = api.getPokemonInfo(name)

            cachedPokemon?.let {
                val updatedPokemon = it.copy(
                    imageUrl = remoteDetails.sprites.frontDefault,
                    hp = remoteDetails.stats.find { s -> s.stat.name == "hp" }?.baseStat,
                    attack = remoteDetails.stats.find { s -> s.stat.name == "attack" }?.baseStat,
                    defense = remoteDetails.stats.find { s -> s.stat.name == "defense" }?.baseStat
                )
                dao.upsert(updatedPokemon)
                Resource.Success(updatedPokemon.toDomain())
            } ?: Resource.Error("Pokemon not found in local DB")

        } catch (e: Exception) {
            e.printStackTrace()
            val cached = dao.getPokemonByName(name)
            if (cached != null) {
                Resource.Success(cached.toDomain())
            } else {
                Resource.Error("Could not load details")
            }
        }
    }
}