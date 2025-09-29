package com.eddy.jedymovieapptest.data.remote.model

import com.google.gson.annotations.SerializedName

data class FilmsDto(
    @SerializedName("Search")
    val results: List<FilmResultDto>
)
