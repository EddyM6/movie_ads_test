package com.eddy.jedymovieapptest.data.di

import androidx.room.Room
import com.eddy.jedymovieapptest.data.local.AppDatabase
import com.eddy.jedymovieapptest.data.local.dao.FilmDao
import org.koin.dsl.module

val localModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java, "app.db"
        ).build()
    }

    single<FilmDao> {
        get<AppDatabase>().filmDao()
    }
}
