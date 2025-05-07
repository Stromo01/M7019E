package com.example.m7019e

import android.content.Context

class FavoriteMovieHandler(private val context: Context) {
    private val jsonParser = JsonParser(context)

    fun addFavoriteMovie(movieId: String) {
        if (!jsonParser.parseMovieJson().any { it.id == movieId }) {
            jsonParser.addMovieToFile(movieId)
        }
    }

    fun removeFavoriteMovie(movieId: String) {
        jsonParser.removeMovieFromFile(movieId)
    }

    fun getFavoriteMovieIds(): List<String> {
        return jsonParser.parseMovieJson().map { it.id }
    }

    fun isFavorite(movieId: String): Boolean {
        return jsonParser.parseMovieJson().any { it.id == movieId }
    }
}