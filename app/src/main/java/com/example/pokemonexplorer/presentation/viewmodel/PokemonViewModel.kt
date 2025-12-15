package com.example.pokemonexplorer.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonexplorer.domain.model.Pokemon
import com.example.pokemonexplorer.domain.repository.PokemonRepository
import com.example.pokemonexplorer.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _state = mutableStateOf(PokemonState())
    val state: State<PokemonState> = _state

    // Εδώ κρατάμε ΟΛΑ τα pokemon που ήρθαν από τη βάση/δίκτυο
    private var allPokemonsCache: List<Pokemon> = emptyList()

    // Πόσα δείχνουμε κάθε φορά
    private var currentDisplayCount = 10
    private val PAGE_SIZE = 10

    private var getPokemonsJob: Job? = null
    private var searchJob: Job? = null

    init {
        getPokemons("fire", fetchFromRemote = true)
    }

    fun onEvent(event: PokemonEvent) {
        when(event) {
            is PokemonEvent.OnTypeChange -> {
                if (_state.value.selectedType != event.type) {
                    // Reset τα πάντα όταν αλλάζει κατηγορία
                    currentDisplayCount = PAGE_SIZE
                    _state.value = _state.value.copy(
                        selectedType = event.type,
                        searchQuery = "",
                        pokemons = emptyList(),
                        isLoading = true
                    )
                    getPokemons(event.type, fetchFromRemote = true)
                }
            }
            is PokemonEvent.OnSearchQueryChange -> {
                _state.value = _state.value.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(300L) // Debounce
                    performLocalSearch(event.query)
                }
            }
            is PokemonEvent.LoadMore -> {
                // Δεν καλω το δίκτυο
                // Απλά αυξάνω το όριο εμφάνισης (Pagination).
                loadNextPage()
            }
        }
    }

    // Η νέα λογική για το Load More
    private fun loadNextPage() {
        val totalItems = allPokemonsCache.size
        if (currentDisplayCount < totalItems) {
            currentDisplayCount += PAGE_SIZE
            updateUiList()
        }
    }

    // Συνάρτηση που κόβει τη λίστα ανάλογα με το search ή το pagination
    private fun updateUiList() {
        val query = _state.value.searchQuery

        // 1. Φιλτράρισμα (Search)
        val filteredList = if (query.isBlank()) {
            allPokemonsCache
        } else {
            allPokemonsCache.filter { it.name.contains(query.trim(), ignoreCase = true) }
        }

        // 2. Σελιδοποίηση (Pagination)
        val paginatedList = filteredList.take(currentDisplayCount)

        _state.value = _state.value.copy(
            pokemons = paginatedList,
            isLoading = false
        )

        // 3. Φέρνω details ΜΟΝΟ για αυτά που εμφανίστηκαν τώρα
        loadDetailsForList(paginatedList)
    }

    private fun performLocalSearch(query: String) {
        updateUiList()
    }

    private fun getPokemons(type: String, fetchFromRemote: Boolean = false) {
        getPokemonsJob?.cancel()

        getPokemonsJob = viewModelScope.launch {
            repository.getPokemons(fetchFromRemote, type)
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            result.data?.let { pokemons ->
                                // Αποθηκεύουμε την πλήρη λίστα στην Cache
                                allPokemonsCache = pokemons
                                // Και ενημερώνουμε το UI με βάση το pagination
                                updateUiList()
                            }
                        }
                        is Resource.Error -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                error = result.message ?: "An unexpected error occurred"
                            )
                        }
                        is Resource.Loading -> {
                            if (allPokemonsCache.isEmpty()) {
                                _state.value = _state.value.copy(isLoading = result.isLoading)
                            }
                        }
                    }
                }
        }
    }

    private fun loadDetailsForList(list: List<Pokemon>) {
        viewModelScope.launch {
            // FIX: Χρησιμοποιούμε 'map' και 'async' για να ξεκινήσουν όλα τα αιτήματα ΤΑΥΤΟΧΡΟΝΑ (Parallel)
            val jobs = list.map { pokemon ->
                async {
                    // Ζητάμε details μόνο αν λείπουν τα stats (το HP είναι null)
                    if (pokemon.hp == null) {
                        repository.getPokemonInfo(pokemon.name)
                    }
                }
            }
            // Περιμένουμε να τελειώσουν όλα (προαιρετικό, αλλά καλή πρακτική για διαχείριση πόρων)
            jobs.awaitAll()
        }
    }
}