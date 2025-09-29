package com.eddy.jedymovieapptest.presentation.screens.favotites.contract

import androidx.compose.runtime.Immutable
import com.eddy.jedymovieapptest.presentation.screens.main.model.FilmListItem

@Immutable
data class FavoritesState(
    val films: List<FilmListItem> = emptyList(),
    val isLoading: Boolean = false
)
