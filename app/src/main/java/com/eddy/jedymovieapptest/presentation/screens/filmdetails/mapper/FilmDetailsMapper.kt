package com.eddy.jedymovieapptest.presentation.screens.filmdetails.mapper

import com.eddy.jedymovieapptest.domain.models.Film
import com.eddy.jedymovieapptest.presentation.screens.filmdetails.model.FilmItem

fun Film.toFilmItem() = FilmItem(
    id = id,
    title = title,
    type = type,
    poster = poster,
    isFavorite = false,
)

fun FilmItem.toFilm() = Film(
    id = id,
    title = title,
    type = type,
    poster = poster,
)
