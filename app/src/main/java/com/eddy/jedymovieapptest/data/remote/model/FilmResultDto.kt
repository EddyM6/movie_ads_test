package com.eddy.jedymovieapptest.data.remote.model

import com.google.gson.annotations.SerializedName

data class FilmResultDto(
    @SerializedName("imdbID")
    val id: String,
    @SerializedName("Title")
    val title: String,
    @SerializedName("Type")
    val type: String,
    @SerializedName("Poster")
    val posterUrl: String,
)
