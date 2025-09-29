package com.eddy.jedymovieapptest.domain.usecase

import com.eddy.jedymovieapptest.domain.models.Film
import com.eddy.jedymovieapptest.domain.repository.FilmsRepository
import com.eddy.jedymovieapptest.domain.resource.Resource
import kotlinx.coroutines.flow.Flow

class SearchFilmsUseCase(
    private val filmsRepository: FilmsRepository
) {
    operator fun invoke(query: String): Flow<Resource<List<Film>>> {
        return filmsRepository.searchFilms(query = query)
    }
}
