package com.eddy.jedymovieapptest.presentation.navigation

sealed class Screen(val route: String) {
    data object Main : Screen(ROUTE_MAIN)
    data object FilmDetails : Screen(ROUTE_DETAILS)
    data object Favorites : Screen(ROUTE_FAVORITES)

    companion object {
        private const val ROUTE_MAIN = "route_main"
        private const val ROUTE_DETAILS = "route_details"
        private const val ROUTE_FAVORITES = "route_favorites"

        const val ARG_FILM = "film_arg"
    }
}
