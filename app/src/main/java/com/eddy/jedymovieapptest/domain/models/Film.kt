package com.eddy.jedymovieapptest.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class Film(
    val id: String,
    val title: String,
    val type: String,
    val poster: String,
): Parcelable
