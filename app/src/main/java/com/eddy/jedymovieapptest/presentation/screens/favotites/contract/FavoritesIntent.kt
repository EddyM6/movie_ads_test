package com.eddy.jedymovieapptest.presentation.screens.favotites.contract


sealed interface FavoritesIntent {
    data object Refresh : FavoritesIntent
    data object BackClicked : FavoritesIntent
}
