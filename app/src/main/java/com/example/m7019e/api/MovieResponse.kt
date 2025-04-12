package com.example.m7019e.api

import android.util.Log
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String,
    val rating: Float,
    val homepage : String = "",
    val imdbid : String = "",
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
            results.add(jsonToMovie(movieJson))
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
            results.add(jsonToMovie(movieJson))
        }
        return results
    }

    fun getMovieDetails(movie:Movie): Movie{
        val response = runBlocking {
            movieApiService.fetchMoviesById(listOf(movie.id.toString()))
        }
        var newMovie: Movie?= null
        val movieJson = response.getJSONObject(0)
        newMovie = jsonToMovie(movieJson)
        return newMovie ?: throw IllegalStateException("No movie details found")
    }

    private fun jsonToMovie(jsonObject: JSONObject): Movie {
        return Movie(
            id = jsonObject.getInt("id"),
            title = jsonObject.getString("title"),
            overview = jsonObject.getString("overview"),
            poster_path = jsonObject.getString("poster_path"),
            rating = "%.1f".format(jsonObject.getDouble("vote_average")).toFloat(),
            homepage = jsonObject.optString("homepage",""),
            imdbid = jsonObject.optString("imdb_id","")
        )

    }

}

