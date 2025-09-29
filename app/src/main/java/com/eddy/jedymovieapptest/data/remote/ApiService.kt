package com.eddy.jedymovieapptest.data.remote

import com.eddy.jedymovieapptest.data.remote.model.FilmsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/")
    suspend fun getSearchedFilms(@Query(QUERY_SEARCH) query: String): Response<FilmsDto>
}
