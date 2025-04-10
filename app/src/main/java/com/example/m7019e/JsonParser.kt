package com.example.m7019e

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
class JsonParser(private val context: android.content.Context) {
    private val movieDBFolder = File(context.filesDir, "MovieDB")
    private val favoritesFile = File(movieDBFolder, "favorites.json")

    init {
        if (!movieDBFolder.exists()) {
            movieDBFolder.mkdir()
        }

        if (!favoritesFile.exists()) {
            favoritesFile.writeText("[]") // Initialize with an empty JSON array
        }
    }

    fun parseMovieJson(): List<String> {
        val movies = mutableListOf<String>()
        try {
            val jsonArray = JSONArray(favoritesFile.readText())
            Log.d("JsonParser", "Parsed JSON: $jsonArray")
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val movieId = jsonObject.getString("id")
                movies.add(movieId)
            }
        } catch (e: Exception) {
            Log.d("JsonParser", "Error parsing JSON: ${e.message}")
        }
        return movies
    }

    fun addMovieToFile(movieId: String) {
        try {
            val jsonArray = JSONArray(favoritesFile.readText())
            val newMovie = JSONObject().put("id", movieId)

            // Check if the movie ID already exists
            for (i in 0 until jsonArray.length()) {
                if (jsonArray.getJSONObject(i).getString("id") == movieId) {
                    Log.d("JsonParser", "Movie ID already exists: $movieId")
                    return
                }
            }

            jsonArray.put(newMovie)
            favoritesFile.writeText(jsonArray.toString())
        } catch (e: Exception) {
            Log.d("JsonParser", "Error adding movie to file: ${e.message}")
        }
    }
    fun removeMovieFromFile(movieId: String) {
        try {
            val jsonArray = JSONArray(favoritesFile.readText())
            val newJsonArray = JSONArray()

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                if (jsonObject.getString("id") != movieId) {
                    newJsonArray.put(jsonObject)
                }
            }

            favoritesFile.writeText(newJsonArray.toString())
        } catch (e: Exception) {
            Log.d("JsonParser", "Error removing movie from file: ${e.message}")
        }
    }
}