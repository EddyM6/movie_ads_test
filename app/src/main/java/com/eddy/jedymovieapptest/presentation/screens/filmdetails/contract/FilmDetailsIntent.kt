package com.eddy.jedymovieapptest.presentation.screens.filmdetails.contract

import com.eddy.jedymovieapptest.domain.models.Film

sealed interface FilmDetailsIntent {
    data class GetFilmDetails(val film: Film) : FilmDetailsIntent
    data object ToggleFavoritesStatus : FilmDetailsIntent
    data object BackClicked : FilmDetailsIntent
}