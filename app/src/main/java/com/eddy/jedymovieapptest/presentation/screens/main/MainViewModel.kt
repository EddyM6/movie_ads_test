package com.eddy.jedymovieapptest.presentation.screens.main

import androidx.lifecycle.viewModelScope
import com.eddy.jedymovieapptest.domain.models.Film
import com.eddy.jedymovieapptest.domain.resource.Resource
import com.eddy.jedymovieapptest.domain.usecase.SearchFilmsUseCase
import com.eddy.jedymovieapptest.presentation.common.BaseViewModel
import com.eddy.jedymovieapptest.presentation.screens.main.contract.MainEffect
import com.eddy.jedymovieapptest.presentation.screens.main.contract.MainIntent
import com.eddy.jedymovieapptest.presentation.screens.main.contract.MainState
import com.eddy.jedymovieapptest.presentation.screens.main.mapper.toFilm
import com.eddy.jedymovieapptest.presentation.screens.main.mapper.toListItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val searchFilmsUseCase: SearchFilmsUseCase
) : BaseViewModel<MainIntent, MainState, MainEffect>(initialState = MainState()) {

    private val searchQuery = MutableStateFlow("")
    private var loadingJob: Job? = null

    init {
        observeSearchQuery()
    }

    override fun handleIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.ChangeSearchQuery -> changeSearchQuery(intent.query)
            MainIntent.Refresh -> refresh()
            is MainIntent.FilmClicked -> onFilmCLicked(intent.film.toFilm())
        }
    }

    private fun changeSearchQuery(query: String) {
        updateState { state -> state.copy(searchQuery = query) }
        searchQuery.update { query }
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            searchQuery
                .debounce(SEARCH_DEBOUNCE_DELAY)
                .filter { it.isNotBlank() }
                .collectLatest(::searchFilms)
        }
    }

    private fun searchFilms(query: String) {
        loadingJob?.cancel()
        loadingJob = searchFilmsUseCase(query)
            .flowOn(Dispatchers.IO)
            .onEach { resource ->
                when (resource) {
                    Resource.Loading -> {
                        updateState { state -> state.copy(isLoading = true) }
                    }
                    is Resource.Error -> {
                        sendEffect(MainEffect.ShowConnectionErrorToast)
                        resource.error.data?.let {
                            updateState { state -> state.copy(films = it.toListItems()) }
                        }
                    }
                    is Resource.Success -> {
                        updateState { state -> state.copy(films = resource.data.toListItems()) }
                    }
                }
            }
            .onCompletion {
                sendEffect(MainEffect.ScrollToTop)
                delay(REFRESH_INDICATOR_DELAY)
                updateState { state -> state.copy(isLoading = false) }
            }
            .flowOn(Dispatchers.Main)
            .launchIn(viewModelScope)
    }

    private fun refresh() {
        updateState { state -> state.copy(films = emptyList()) }
        searchFilms(currentState.searchQuery)
    }
    private fun onFilmCLicked(film: Film) {
        viewModelScope.launch {
            sendEffect(MainEffect.NavigateToDetails(film = film))
        }
    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 500L
        private const val REFRESH_INDICATOR_DELAY = 500L
    }
}
