package com.eddy.jedymovieapptest.presentation.screens.main.contract

import com.eddy.jedymovieapptest.domain.models.Film

sealed interface MainEffect {
    data class NavigateToDetails(val film: Film) : MainEffect
    data object ShowConnectionErrorToast : MainEffect
    data object ScrollToTop : MainEffect
}
