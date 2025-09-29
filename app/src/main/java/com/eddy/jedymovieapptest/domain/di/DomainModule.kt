package com.eddy.jedymovieapptest.domain.di

import com.eddy.jedymovieapptest.domain.usecase.FavoriteFilmsUseCase
import com.eddy.jedymovieapptest.domain.usecase.SearchFilmsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::SearchFilmsUseCase)
    factoryOf(::FavoriteFilmsUseCase)
}
