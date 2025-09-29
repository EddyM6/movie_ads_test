package com.eddy.jedymovieapptest.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eddy.jedymovieapptest.data.local.entity.FilmEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilm(film: FilmEntity)

    @Query("SELECT * FROM fav_films")
    fun getAll(): Flow<List<FilmEntity>>

    @Query("DELETE FROM fav_films WHERE id = :filmId")
    suspend fun removeById(filmId: String)

    @Query("SELECT * FROM fav_films WHERE id = :filmId")
    suspend fun findById(filmId: String): FilmEntity?
}