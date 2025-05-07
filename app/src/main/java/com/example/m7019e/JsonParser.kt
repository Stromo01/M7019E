package com.example.m7019e

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File

@Serializable
data class FavoriteMovie(val id: String)


class JsonParser(private val context: Context) {
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

    fun parseMovieJson(): List<FavoriteMovie> {
        return try {
            val jsonText = favoritesFile.readText()
            Json.decodeFromString(jsonText)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun addMovieToFile(movieId: String) {
        val movies = parseMovieJson().toMutableList()
        if (movies.none { it.id == movieId }) {
            movies.add(FavoriteMovie(movieId))
            favoritesFile.writeText(Json.encodeToString(movies))
        }
    }

    fun removeMovieFromFile(movieId: String) {
        val movies = parseMovieJson().filter { it.id != movieId }
        favoritesFile.writeText(Json.encodeToString(movies))
    }
}