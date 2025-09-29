package com.eddy.jedymovieapptest.data.repository

import com.eddy.jedymovieapptest.data.local.dao.FilmDao
import com.eddy.jedymovieapptest.data.mappers.toEntity
import com.eddy.jedymovieapptest.data.mappers.toFilm
import com.eddy.jedymovieapptest.data.mappers.toFilms
import com.eddy.jedymovieapptest.data.remote.ApiService
import com.eddy.jedymovieapptest.data.repository.util.safeApiCall
import com.eddy.jedymovieapptest.domain.models.Film
import com.eddy.jedymovieapptest.domain.repository.FilmsRepository
import com.eddy.jedymovieapptest.domain.resource.Resource
import com.eddy.jedymovieapptest.domain.resource.RootError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlin.collections.map

class FilmsRepositoryImpl(
    private val apiService: ApiService,
    private val filmDao: FilmDao
) : FilmsRepository {
    override fun searchFilms(query: String): Flow<Resource<List<Film>>> = safeApiCall(
        apiCall = { apiService.getSearchedFilms(query) },
        dtoMapper = { dto -> dto.toFilms() },
    )

    override suspend fun addToFavorite(film: Film): Resource<Unit> {
        return try {
            filmDao.insertFilm(film.toEntity())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(RootError.Error(e))
        }
    }

    override suspend fun isFavorite(filmId: String): Resource<Boolean> {
        return try {
            val foundItem = filmDao.findById(filmId)
            Resource.Success(foundItem != null)
        } catch (e: Exception) {
            Resource.Error(RootError.Error(e))
        }
    }

    override suspend fun removeFromFavorite(filmId: String): Resource<Unit> {
        return try {
            filmDao.removeById(filmId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(RootError.Error(e))
        }
    }

    override fun getAllFavorites() = filmDao.getAll().transform {
        emit(Resource.Loading)
        emit(Resource.Success(it.map { entity -> entity.toFilm() }))
    }.catch { emit(Resource.Error(error = RootError.Error(it))) }
}
