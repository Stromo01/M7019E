package com.example.m7019e.api
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException
import org.json.JSONObject

class MovieApiService {
    val client = OkHttpClient()
    val apiKey = "ccc6a0ec53372cf3a8056fa7c63d72ed"

    suspend fun fetchTrendingMovies(category: String): JSONArray {
        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/movie/$category?api_key=$apiKey&language=en-US")
            .build()

        return withContext(Dispatchers.IO) {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val jsonResponse = JSONObject(response.body?.string())
                jsonResponse.getJSONArray("results")
            }
        }
    }
    suspend fun fetchGenres(): JSONArray {
        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/genre/movie/list?api_key=$apiKey&language=en-US")
            .build()

        return withContext(Dispatchers.IO) {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val jsonResponse = JSONObject(response.body?.string())
                jsonResponse.getJSONArray("genres")
            }
        }
    }

    suspend fun fetchMoviesById(ids: List<String>): JSONArray {
        val results = JSONArray()
        withContext(Dispatchers.IO) {
            ids.forEach { id ->
                val request = Request.Builder()
                    .url("https://api.themoviedb.org/3/movie/$id?api_key=$apiKey&language=en-US")
                    .build()
                Log.d("fetchMoviesById", "https://api.themoviedb.org/3/movie/$id?api_key=$apiKey&language=en-US")
                try {
                    client.newCall(request).execute().use { response ->
                        if (response.isSuccessful) {
                            val jsonResponse = JSONObject(response.body?.string())
                            val movie = jsonResponse
                            if (movie.length() > 0) {
                                results.put(movie) // Add the first movie result
                            } else {
                                Log.d("fetchMoviesById", "No movie results found for ID: $id")
                            }
                        } else {
                            Log.e("fetchMoviesById", "Failed to fetch movie for ID: $id" +
                                    " - Response code: ${response.code}")
                        }
                    }
                } catch (e: IOException) {
                    Log.e("fetchMoviesById", "Error fetching movie for ID: $id", e)
                }
            }
        }
        return results
    }
}