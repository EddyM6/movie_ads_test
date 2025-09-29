package com.eddy.jedymovieapptest.presentation.screens.filmdetails.contract
import androidx.compose.runtime.Immutable
import com.eddy.jedymovieapptest.presentation.screens.filmdetails.model.FilmItem

@Immutable
data class FilmDetailsState(
    val film: FilmItem? = null
)