package com.eddy.jedymovieapptest.domain.repository

import com.eddy.jedymovieapptest.domain.models.Film
import com.eddy.jedymovieapptest.domain.resource.Resource
import kotlinx.coroutines.flow.Flow

interface FilmsRepository {
    fun searchFilms(query: String): Flow<Resource<List<Film>>>

    suspend fun addToFavorite(film: Film): Resource<Unit>
    suspend fun isFavorite(filmId: String): Resource<Boolean>
    suspend fun removeFromFavorite(filmId: String): Resource<Unit>

    fun getAllFavorites(): Flow<Resource<List<Film>>>
}