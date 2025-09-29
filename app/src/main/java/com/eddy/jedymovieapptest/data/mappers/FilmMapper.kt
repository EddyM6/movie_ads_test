package com.eddy.jedymovieapptest.data.mappers

import com.eddy.jedymovieapptest.data.local.entity.FilmEntity
import com.eddy.jedymovieapptest.data.remote.model.FilmResultDto
import com.eddy.jedymovieapptest.data.remote.model.FilmsDto
import com.eddy.jedymovieapptest.domain.models.Film

fun FilmsDto.toFilms() = results.map { it.toFilm() }

fun FilmResultDto.toFilm() = Film(
    id = id,
    title = title,
    type = type,
    poster = posterUrl
)

fun FilmEntity.toFilm() = Film(
    id = id,
    title = title,
    type = type,
    poster = poster
)

fun Film.toEntity() = FilmEntity(
    id = id,
    title = title,
    type = type,
    poster = poster
)
