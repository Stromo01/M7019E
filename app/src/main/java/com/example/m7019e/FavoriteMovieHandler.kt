package com.example.m7019e

class FavoriteMovieHandler {

    private var movieIds = mutableListOf(
        "tt0111161", // The Shawshank Redemption
        "tt0068646", // The Godfather
        "tt0071562", // The Godfather Part II
        "tt0468569", // The Dark Knight
        "tt0050083", // 12 Angry Men
        "tt0108052", // Schindler's List
        "tt0167260", // The Lord of the Rings: The Return of the King
        "tt0110912", // Pulp Fiction
        "tt0060196", // The Good, the Bad and the Ugly
        "tt0137523"  // Fight Club

    )

    fun addFavoriteMovie(movieId: String) {
        if (!movieIds.contains(movieId)) {
            movieIds.add(movieId)
        }
    }

    fun removeFavoriteMovie(movieId: String) {
        movieIds.remove(movieId)
    }

    fun getFavoriteMovieIds(): List<String> {
        return movieIds
    }
}