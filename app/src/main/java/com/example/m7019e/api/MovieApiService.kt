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
    suspend fun fetchTrendingMovies(): JSONArray {
        val client = OkHttpClient()
        val api_key = "ccc6a0ec53372cf3a8056fa7c63d72ed"
        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/trending/movie/day?api_key=$api_key&language=en-US")
            .build()

        return withContext(Dispatchers.IO) {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val jsonResponse = JSONObject(response.body?.string())
                jsonResponse.getJSONArray("results")
            }
        }
    }
}