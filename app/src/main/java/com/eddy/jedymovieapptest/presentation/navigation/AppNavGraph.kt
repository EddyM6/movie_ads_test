package com.eddy.jedymovieapptest.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eddy.jedymovieapptest.domain.models.Film
import com.eddy.jedymovieapptest.presentation.screens.favotites.FavoriteScreen
import com.eddy.jedymovieapptest.presentation.screens.filmdetails.FilmDetailsScreen
import com.eddy.jedymovieapptest.presentation.screens.main.MainScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(route = Screen.Main.route) {
            MainScreen(
                navigateToDetails = {
                    navController.currentBackStackEntry?.savedStateHandle?.set(Screen.ARG_FILM, it)
                    navController.navigate(route = Screen.FilmDetails.route)
                },
                navigateToFavorites = {
                    navController.navigate(route = Screen.Favorites.route)
                }
            )
        }

        composable(route = Screen.FilmDetails.route) { entry ->
            val film = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<Film>(Screen.ARG_FILM)
            if (film != null) {
                FilmDetailsScreen(
                    film = film,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }

        composable(route = Screen.Favorites.route) {
            FavoriteScreen(
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}