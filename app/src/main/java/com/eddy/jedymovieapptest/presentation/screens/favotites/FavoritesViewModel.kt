package com.eddy.jedymovieapptest.presentation.screens.favotites

import androidx.lifecycle.viewModelScope
import com.eddy.jedymovieapptest.domain.models.Film
import com.eddy.jedymovieapptest.domain.resource.Resource
import com.eddy.jedymovieapptest.domain.usecase.FavoriteFilmsUseCase
import com.eddy.jedymovieapptest.domain.usecase.SearchFilmsUseCase
import com.eddy.jedymovieapptest.presentation.common.BaseViewModel
import com.eddy.jedymovieapptest.presentation.screens.favotites.contract.FavoritesEffect
import com.eddy.jedymovieapptest.presentation.screens.favotites.contract.FavoritesIntent
import com.eddy.jedymovieapptest.presentation.screens.favotites.contract.FavoritesState
import com.eddy.jedymovieapptest.presentation.screens.filmdetails.contract.FilmDetailsEffect
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
import kotlinx.coroutines.withContext

class FavoritesViewModel(
    private val favoriteFilmsUseCase: FavoriteFilmsUseCase
) : BaseViewModel<FavoritesIntent, FavoritesState, FavoritesEffect>(initialState = FavoritesState()) {

    private var loadingJob: Job? = null

    init {
        observeFavoritesFilm()
    }

    override fun handleIntent(intent: FavoritesIntent) {
        when (intent) {
            FavoritesIntent.BackClicked -> onBackClicked()
            FavoritesIntent.Refresh -> imitateRefresh()
        }
    }

    private fun observeFavoritesFilm() {
        loadingJob?.cancel()
        loadingJob = favoriteFilmsUseCase.getFavorites()
            .flowOn(Dispatchers.IO)
            .onEach { resource ->
                when (resource) {
                    Resource.Loading -> {
                        updateState { state -> state.copy(isLoading = true) }
                    }
                    is Resource.Error -> {
                        sendEffect(FavoritesEffect.ShowErrorToast)
                        resource.error.data?.let {
                            updateState { state -> state.copy(films = it.toListItems(), isLoading = false) }
                        }
                    }
                    is Resource.Success -> {
                        updateState { state -> state.copy(films = resource.data.toListItems(), isLoading = false) }
                    }
                }
            }
            .onCompletion {
                delay(REFRESH_INDICATOR_DELAY)
                updateState { state -> state.copy(isLoading = false) }
            }
            .flowOn(Dispatchers.Main)
            .launchIn(viewModelScope)
    }

    private fun imitateRefresh() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(REFRESH_INDICATOR_DELAY)
            withContext(Dispatchers.Main.immediate) {
                updateState { state -> state.copy(isLoading = false) }
            }
        }
    }

    private fun onBackClicked() {
        viewModelScope.launch {
            sendEffect(FavoritesEffect.NavigateBack)
        }
    }


    companion object {
        private const val REFRESH_INDICATOR_DELAY = 500L
    }
}
