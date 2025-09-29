package com.eddy.jedymovieapptest.presentation.screens.filmdetails.contract
sealed interface FilmDetailsEffect {
    data object NavigateBack : FilmDetailsEffect
}
