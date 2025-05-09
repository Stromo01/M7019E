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
    val genre_names: String = ""
)

data class Review(
    val author: String,
    val content: String,
    val rating: Float,
    val created_at: String
)

data class Video(
    val key: String,
    val name: String
)

class MovieResponse{ //Takes api response and converts it to a list of movies
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
        val genreNames = mutableListOf<String>()

        if (jsonObject.has("genres")) {
            val genresArray = jsonObject.optJSONArray("genres")
            if (genresArray != null) {
                for (i in 0 until genresArray.length()) {
                    val genre = genresArray.getJSONObject(i)
                    genreNames.add(genre.getString("name"))
                }
            }
        }

        return Movie(
            id = jsonObject.getInt("id"),
            title = jsonObject.getString("title"),
            overview = jsonObject.getString("overview"),
            poster_path = jsonObject.getString("poster_path"),
            rating = "%.1f".format(jsonObject.getDouble("vote_average")).toFloat(),
            homepage = jsonObject.optString("homepage", ""),
            imdbid = jsonObject.optString("imdb_id", ""),
            genre_names = genreNames.joinToString(", ")
        )
    }

    fun getVideos(movie: Movie): List<Video> {
        val results = mutableListOf<Video>()
        val response = runBlocking {
            movieApiService.fetchVideos(movie.id.toString())
        }
        for (i in 0 until response.length()) {
            val videoJson = response.getJSONObject(i)
            val video = Video(
                key = videoJson.getString("key"),
                name = videoJson.getString("name")
            )
            results.add(video)
        }
        return results
    }
    fun getReviews(movie: Movie): List<Review> {
        val results = mutableListOf<Review>()
        val response = runBlocking {
            movieApiService.fetchReviews(movie.id.toString())
        }
        for (i in 0 until response.length()) {
            val reviewJson = response.getJSONObject(i)
            val rating = if (reviewJson.getJSONObject("author_details").isNull("rating")) {
                0.0f // Default value for null rating
            } else {
                "%.1f".format(reviewJson.getJSONObject("author_details").getDouble("rating")).toFloat()
            }
            results.add(
                Review(
                    author = reviewJson.getString("author"),
                    content = reviewJson.getString("content"),
                    rating = rating,
                    created_at = reviewJson.getString("created_at")
                )
            )
        }
        return results
    }

}

