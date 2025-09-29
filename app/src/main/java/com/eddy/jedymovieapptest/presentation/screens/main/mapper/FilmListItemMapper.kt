package com.eddy.jedymovieapptest.presentation.screens.main.mapper

import com.eddy.jedymovieapptest.domain.models.Film
import com.eddy.jedymovieapptest.presentation.screens.main.model.FilmListItem

fun List<Film>.toListItems() = map { it.toListItem() }

fun Film.toListItem() = FilmListItem(id = id, title = title, type = type, poster = poster)
fun FilmListItem.toFilm() = Film(id = id, title = title, type = type, poster = poster)
