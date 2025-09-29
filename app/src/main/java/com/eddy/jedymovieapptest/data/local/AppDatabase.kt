package com.eddy.jedymovieapptest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eddy.jedymovieapptest.data.local.dao.FilmDao
import com.eddy.jedymovieapptest.data.local.entity.FilmEntity

@Database(entities = [FilmEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
}
