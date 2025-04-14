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
    val genre_ids: List<Int> = emptyList(),
    val genre_names: String = ""
)

class MovieResponse{
    val movieApiService = MovieApiService()
    private val genreMap = mutableMapOf<Int, String>()

    init {
        runBlocking {
            loadGenres()
        }
    }
    suspend fun loadGenres() {
        val genres = movieApiService.fetchGenres()
        for (i in 0 until genres.length()) {
            val genre = genres.getJSONObject(i)
            genreMap[genre.getInt("id")] = genre.getString("name")
        }
    }

    fun genreIdsToNames(ids: List<Int>): String {
        return ids.mapNotNull { genreMap[it] }.joinToString(", ")
    }


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

    fun getMovieDetails(movie: Movie): Movie {
        val response = runBlocking {
            // Load genres before fetching details (if not already loaded)
            movieApiService.fetchMoviesById(listOf(movie.id.toString()))
        }
        val movieJson = response.getJSONObject(0)
        val newMovie = jsonToMovie(movieJson)
        return newMovie
    }

    private fun jsonToMovie(jsonObject: JSONObject): Movie {
        val genreIdsJsonArray = jsonObject.optJSONArray("genre_ids")
        val genreIds = mutableListOf<Int>()
        val genreNames = mutableListOf<String>()

        if (jsonObject.has("genres")) {
            val genresArray = jsonObject.optJSONArray("genres")
            if (genresArray != null) {
                for (i in 0 until genresArray.length()) {
                    val genre = genresArray.getJSONObject(i)
                    genreIds.add(genre.getInt("id"))
                    genreNames.add(genre.getString("name"))
                }
            }
        } else if (jsonObject.has("genre_ids")) {
            // Case 2: List endpoint with genre IDs only
            val idsArray = jsonObject.optJSONArray("genre_ids")
            if (idsArray != null) {
                for (i in 0 until idsArray.length()) {
                    genreIds.add(idsArray.getInt(i))
                }
            }
        }

        return Movie(
            id = jsonObject.getInt("id"),
            title = jsonObject.getString("title"),
            overview = jsonObject.getString("overview"),
            poster_path = jsonObject.getString("poster_path"),
            rating = "%.1f".format(jsonObject.getDouble("vote_average")).toFloat(),
            homepage = jsonObject.optString("homepage",""),
            imdbid = jsonObject.optString("imdb_id",""),
            genre_ids = genreIds,
            genre_names = genreIdsToNames(genreIds)
        )
    }

}

