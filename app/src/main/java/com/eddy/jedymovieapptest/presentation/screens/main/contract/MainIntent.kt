package com.eddy.jedymovieapptest.presentation.screens.main.contract

import com.eddy.jedymovieapptest.domain.models.Film
import com.eddy.jedymovieapptest.presentation.screens.main.model.FilmListItem

sealed interface MainIntent {
    data class ChangeSearchQuery(val query: String) : MainIntent
    data object Refresh : MainIntent
    data class FilmClicked(val film: FilmListItem) : MainIntent
}