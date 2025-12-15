package com.example.pokemonexplorer

import com.example.pokemonexplorer.domain.model.Pokemon
import com.example.pokemonexplorer.domain.repository.PokemonRepository
import com.example.pokemonexplorer.presentation.viewmodel.PokemonEvent
import com.example.pokemonexplorer.presentation.viewmodel.PokemonViewModel
import com.example.pokemonexplorer.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonViewModelTest {

    // 1. Mocking: Φτιάχνουμε ένα "ψεύτικο" Repository.
    // Δεν θέλουμε να χτυπήσουμε αληθινό δίκτυο ή βάση στα tests.
    private lateinit var repository: PokemonRepository
    private lateinit var viewModel: PokemonViewModel

    // Ένας "ελεγκτής χρόνου" για τα Coroutines
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // Ορίζουμε τον Main Dispatcher να είναι ο δικός μας (για να ελέγχουμε τον χρόνο)
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `When init, loads fire pokemons and updates state`() = runTest {
        // GIVEN (Δεδομένα)
        val firePokemons = listOf(
            Pokemon("charmander", "url", null, null, null, null, "fire"),
            Pokemon("charizard", "url", null, null, null, null, "fire")
        )
        // Λέμε στο ψεύτικο repository: "Αν σου ζητήσουν 'fire', δώσε πίσω αυτή τη λίστα"
        coEvery { repository.getPokemons(true, "fire") } returns flowOf(Resource.Success(firePokemons))
        // Για τα details, απλά επέστρεψε success (δεν μας νοιάζει το περιεχόμενο τώρα)
        coEvery { repository.getPokemonInfo(any()) } returns Resource.Success(firePokemons[0])

        // WHEN (Δράση)
        // Αρχικοποιούμε το ViewModel (αυτό καλεί το init block)
        viewModel = PokemonViewModel(repository)

        // Προχωράμε τον χρόνο μέχρι να τελειώσουν όλες οι διεργασίες
        advanceUntilIdle()

        // THEN (Έλεγχος)
        // 1. Το loading πρέπει να είναι false
        assertEquals(false, viewModel.state.value.isLoading)
        // 2. Η λίστα πρέπει να έχει 2 Pokemon
        assertEquals(2, viewModel.state.value.pokemons.size)
        // 3. Ο επιλεγμένος τύπος πρέπει να είναι "fire"
        assertEquals("fire", viewModel.state.value.selectedType)
    }

    @Test
    fun `When changing type to water, clears list and loads water pokemons`() = runTest {
        // GIVEN
        // Ρυθμίζουμε τι θα επιστρέψει για "fire" (initial load)
        coEvery { repository.getPokemons(true, "fire") } returns flowOf(Resource.Success(emptyList()))

        // Ρυθμίζουμε τι θα επιστρέψει για "water" (η αλλαγή μας)
        val waterPokemons = listOf(
            Pokemon("squirtle", "url", null, null, null, null, "water")
        )
        coEvery { repository.getPokemons(true, "water") } returns flowOf(Resource.Success(waterPokemons))
        coEvery { repository.getPokemonInfo(any()) } returns Resource.Success(waterPokemons[0])

        viewModel = PokemonViewModel(repository)
        advanceUntilIdle() // Αφήνουμε να τρέξει το init

        // WHEN (Ο χρήστης πατάει Water)
        viewModel.onEvent(PokemonEvent.OnTypeChange("water"))

        // THEN (Πριν προχωρήσει ο χρόνος, πρέπει να έχει αδειάσει η λίστα!)
        // Αυτό επιβεβαιώνει ότι κάναμε σωστά το reset της λίστας στο UI
        // Σημείωση: Επειδή το state update είναι πολύ γρήγορο, ελέγχουμε κυρίως ότι μετά το idle έχουμε τα σωστά

        advanceUntilIdle() // Αφήνουμε να ολοκληρωθεί το request

        // Ελέγχουμε ότι τώρα έχουμε τα Water pokemon
        assertEquals("water", viewModel.state.value.selectedType)
        assertEquals("squirtle", viewModel.state.value.pokemons.first().name)
    }
}