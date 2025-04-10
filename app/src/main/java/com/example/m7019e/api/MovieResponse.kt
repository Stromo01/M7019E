package com.example.m7019e.api

import android.util.Log
import kotlinx.coroutines.runBlocking

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String,
    val rating: Float
)
class MovieResponse{
    val movieApiService = MovieApiService()


    fun getMovies(category: String): List<Movie>  {
        Log.d("getMovies", "Fetching movies")

        val results = mutableListOf<Movie>()
        val response = runBlocking {
            movieApiService.fetchTrendingMovies(category)
        }
        for (i in 0 until response.length()) {
            val movieJson = response.getJSONObject(i)
            val movie = Movie(
                id = movieJson.getInt("id"),
                title = movieJson.getString("title"),
                overview = movieJson.getString("overview"),
                poster_path = movieJson.getString("poster_path"),
                rating = "%.1f".format(movieJson.getDouble("vote_average")).toFloat()
            )
            results.add(movie)
        }
        return results
    }
    fun getFavoriteMovies(idList: List<String>): List<Movie> { // returns a list of favorite movies and info
        Log.d("getMovies", "Fetching favorite movies $idList")
        val results = mutableListOf<Movie>()
        val response = runBlocking {
            movieApiService.fetchMoviesById(idList)
        }
        for (i in 0 until response.length()) {
            val movieJson = response.getJSONObject(i)
            val movie = Movie(
                id = movieJson.getInt("id"),
                title = movieJson.getString("title"),
                overview = movieJson.getString("overview"),
                poster_path = movieJson.getString("poster_path"),
                rating = "%.1f".format(movieJson.getDouble("vote_average")).toFloat()
            )
            results.add(movie)
        }
        return results
    }
}

