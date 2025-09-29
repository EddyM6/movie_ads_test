package com.eddy.jedymovieapptest.presentation.screens.main.contract

import androidx.compose.runtime.Immutable
import com.eddy.jedymovieapptest.presentation.screens.main.model.FilmListItem

@Immutable
data class MainState(
    val films: List<FilmListItem> = emptyList(),
    val searchMode: Boolean = true,
    val searchQuery: String = "",
    val isLoading: Boolean = false
)
