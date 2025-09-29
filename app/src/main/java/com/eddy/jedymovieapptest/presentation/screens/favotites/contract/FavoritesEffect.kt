package com.eddy.jedymovieapptest.presentation.screens.favotites.contract

import com.eddy.jedymovieapptest.domain.models.Film

sealed interface FavoritesEffect {
    data object NavigateBack: FavoritesEffect
    data object ShowErrorToast : FavoritesEffect
}
