package com.eddy.jedymovieapptest.data.di

import org.koin.dsl.module

val dataModule = module {
    includes(networkModule, localModule, repositoryModule)
}
