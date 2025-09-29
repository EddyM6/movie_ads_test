package com.eddy.jedymovieapptest.domain.usecase

import com.eddy.jedymovieapptest.domain.models.Film
import com.eddy.jedymovieapptest.domain.repository.FilmsRepository

class FavoriteFilmsUseCase(
    private val filmsRepository: FilmsRepository
) {
    suspend fun isFavorite(filmId: String) = filmsRepository.isFavorite(filmId)
    suspend fun addToFavorite(film: Film) = filmsRepository.addToFavorite(film)
    suspend fun removeFromFavorite(filmId: String) = filmsRepository.removeFromFavorite(filmId)

    fun getFavorites() = filmsRepository.getAllFavorites()
}
