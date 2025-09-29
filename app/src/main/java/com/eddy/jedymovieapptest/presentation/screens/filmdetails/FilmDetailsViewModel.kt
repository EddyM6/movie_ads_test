package com.eddy.jedymovieapptest.presentation.screens.filmdetails

import androidx.lifecycle.viewModelScope
import com.eddy.jedymovieapptest.domain.models.Film
import com.eddy.jedymovieapptest.domain.resource.Resource
import com.eddy.jedymovieapptest.domain.usecase.FavoriteFilmsUseCase
import com.eddy.jedymovieapptest.presentation.common.BaseViewModel
import com.eddy.jedymovieapptest.presentation.screens.filmdetails.contract.FilmDetailsEffect
import com.eddy.jedymovieapptest.presentation.screens.filmdetails.contract.FilmDetailsIntent
import com.eddy.jedymovieapptest.presentation.screens.filmdetails.contract.FilmDetailsState
import com.eddy.jedymovieapptest.presentation.screens.filmdetails.mapper.toFilm
import com.eddy.jedymovieapptest.presentation.screens.filmdetails.mapper.toFilmItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilmDetailsViewModel(
    private val favoriteFilmsUseCase: FavoriteFilmsUseCase
) : BaseViewModel<FilmDetailsIntent, FilmDetailsState, FilmDetailsEffect>(initialState = FilmDetailsState()) {

    override fun handleIntent(intent: FilmDetailsIntent) {
        when (intent) {
            is FilmDetailsIntent.GetFilmDetails -> getFilmDetails(intent.film)
            FilmDetailsIntent.ToggleFavoritesStatus -> toggleFavoritesStatus()
            FilmDetailsIntent.BackClicked -> onBackClicked()
        }
    }

    private fun getFilmDetails(film: Film) {
        updateState { state -> state.copy(film = film.toFilmItem())}
        viewModelScope.launch(Dispatchers.IO) {
            val isFavorite = favoriteFilmsUseCase.isFavorite(film.id)
            currentState.film?.let { currentFilm ->
                when (isFavorite) {
                    is Resource.Error<*> -> withContext(Dispatchers.Main.immediate) {
                        updateState { state -> state.copy(film = currentFilm.copy(isFavorite = false)) }
                    }
                    is Resource.Success -> withContext(Dispatchers.Main.immediate) {
                        updateState { state -> state.copy(film = currentFilm.copy(isFavorite = isFavorite.data)) }
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun toggleFavoritesStatus() {
        currentState.film?.let { film ->
            val isNowFav = !film.isFavorite
            updateState { state -> state.copy(film = film.copy(isFavorite = isNowFav)) }
            viewModelScope.launch(Dispatchers.IO) {
                if (isNowFav) {
                    favoriteFilmsUseCase.addToFavorite(film.toFilm())
                } else {
                    favoriteFilmsUseCase.removeFromFavorite(film.id)
                }
            }
        }
    }

    private fun onBackClicked() {
        viewModelScope.launch {
            sendEffect(FilmDetailsEffect.NavigateBack)
        }
    }
}
