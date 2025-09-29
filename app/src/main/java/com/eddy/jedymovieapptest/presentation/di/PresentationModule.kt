package com.eddy.jedymovieapptest.presentation.di

import com.eddy.jedymovieapptest.presentation.screens.favotites.FavoritesViewModel
import com.eddy.jedymovieapptest.presentation.screens.filmdetails.FilmDetailsViewModel
import com.eddy.jedymovieapptest.presentation.screens.main.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::FilmDetailsViewModel)
    viewModelOf(::FavoritesViewModel)
}
