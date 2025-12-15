package com.example.pokemonexplorer.di

import android.app.Application
import androidx.room.Room
import com.example.pokemonexplorer.data.local.PokemonDatabase
import com.example.pokemonexplorer.data.remote.PokeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePokeApi(): PokeApi {
        // 1. Δημιουργούμε τον "ωτακουστή" (Interceptor) για να βλέπουμε τα Logs
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Καταγράφει τα πάντα (Headers + Body)
        }

        // 2. Τον προσθέτουμε στον Client
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client) // <--- ΠΟΛΥ ΣΗΜΑΝΤΙΚΟ: Συνδέουμε τον client
            .build()
            .create(PokeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(app: Application): PokemonDatabase {
        return Room.databaseBuilder(
            app,
            PokemonDatabase::class.java,
            "pokemon_db"
        ).build()
    }
}