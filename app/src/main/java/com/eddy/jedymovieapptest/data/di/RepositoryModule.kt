package com.eddy.jedymovieapptest.data.di

import com.eddy.jedymovieapptest.data.repository.FilmsRepositoryImpl
import com.eddy.jedymovieapptest.domain.repository.FilmsRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::FilmsRepositoryImpl) { bind<FilmsRepository>() }
}
