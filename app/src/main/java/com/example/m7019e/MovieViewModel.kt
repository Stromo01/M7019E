package com.example.m7019e

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import com.example.m7019e.api.Movie
import com.example.m7019e.api.MovieResponse

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val movieDao = AppDatabase.getDatabase(application).movieDao()

    var selectedMovie = mutableStateOf<Movie?>(null)
        private set

    fun selectMovie(movie: Movie) {
        selectedMovie.value = movie
    }

    fun cacheMoviesByCategory(category: String, movies: List<MovieEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.deleteMoviesByCategory(category)
            movieDao.insertMovies(movies)
        }
    }
    fun fetchMovies(category: String, movieResponse: MovieResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movies = movieResponse.getMovies(category)
                cacheMoviesByCategory(category, movies.mapIndexed { index, movie -> movie.toEntity(category, index) })
            } catch (e: Exception) {
                // Handle error (e.g., no internet)
            }
        }
    }
    fun getMovies(category: String, movieResponse: MovieResponse, callback: (List<Movie>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val movies = try {
                fetchMovies(category, movieResponse)
                movieResponse.getMovies(category)
            } catch (e: Exception) {
                getCachedMoviesByCategory(category).map { it.toDomain() }
            }
            callback(movies)
        }
    }
    suspend fun getCachedMoviesByCategory(category: String): List<MovieEntity> {
        return movieDao.getMoviesByCategory(category)
    }
}