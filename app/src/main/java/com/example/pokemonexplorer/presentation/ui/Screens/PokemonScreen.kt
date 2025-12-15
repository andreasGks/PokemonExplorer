package com.example.pokemonexplorer.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pokemonexplorer.presentation.ui.components.PokemonItem
import com.example.pokemonexplorer.presentation.ui.components.TypeChip
import com.example.pokemonexplorer.presentation.viewmodel.PokemonEvent
import com.example.pokemonexplorer.presentation.viewmodel.PokemonViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonScreen(
    viewModel: PokemonViewModel = hiltViewModel()
) {
    // Παίρνουμε το state
    val state = viewModel.state.value

    // FIX 1: Χρησιμοποιούμε 'remember' για να μην ξαναφτιάχνεται η λίστα σε κάθε αλλαγή του UI
    val pokemonTypes = remember {
        listOf(
            "fire", "water", "grass", "electric", "dragon",
            "psychic", "ghost", "dark", "steel", "fairy"
        )
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Search Field
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { query ->
                        viewModel.onEvent(PokemonEvent.OnSearchQueryChange(query))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search Pokemon...") },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Type Selector (Horizontal List)
                Text(
                    text = "Select Type:",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // FIX 2: Προσθέσαμε 'key'. Τώρα το Compose ξέρει ότι αυτά τα items δεν αλλάζουν.
                    items(
                        items = pokemonTypes,
                        key = { it } // Το όνομα του τύπου είναι μοναδικό
                    ) { type ->
                        TypeChip(
                            type = type,
                            isSelected = state.selectedType == type,
                            onClick = {
                                viewModel.onEvent(PokemonEvent.OnTypeChange(type))
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->

        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {

            // Empty State
            if (state.pokemons.isEmpty() && !state.isLoading) {
                Text(
                    text = "No Pokemon found.",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                // Pokemon List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // FIX 3: Προσθέσαμε 'key' και εδώ για να μην τρεμοπαίζει η λίστα όταν φορτώνουν οι εικόνες
                    items(
                        items = state.pokemons,
                        key = { it.name } // Το όνομα του Pokemon είναι μοναδικό
                    ) { pokemon ->
                        PokemonItem(pokemon = pokemon)
                    }

                    // Load More Button (Visible if we have items)
                    if (state.pokemons.isNotEmpty()) {
                        item(key = "load_more_button") {
                            Button(
                                onClick = {
                                    viewModel.onEvent(PokemonEvent.LoadMore)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            ) {
                                Text("Load More")
                            }
                        }
                    }
                }
            }

            // Loading Indicator
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            // Error Message (Snackbar style)
            state.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                )
            }
        }
    }
}