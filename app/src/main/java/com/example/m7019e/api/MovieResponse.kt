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

fun getMovies(): List<Movie>  {
    Log.d("getMovies", "Fetching movies")
    val movieApiService = MovieApiService()
    val results = mutableListOf<Movie>()
    val response = runBlocking {
        movieApiService.fetchTrendingMovies()
    }
    for (i in 0 until 10){//response.length()) {
        val movieJson = response.getJSONObject(i)
        val movie = Movie(
            id = movieJson.getInt("id"),
            title = movieJson.getString("title"),
            overview = movieJson.getString("overview"),
            poster_path = movieJson.getString("poster_path"),
            rating = movieJson.getDouble("vote_average").toFloat()
        )
        results.add(movie)
    }
    return results
}