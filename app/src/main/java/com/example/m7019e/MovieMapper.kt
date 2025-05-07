package com.example.m7019e

import com.example.m7019e.api.MovieResponse
import com.example.m7019e.api.Movie
import com.example.m7019e.MovieEntity

fun Movie.toEntity(category: String): MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title,
        overview = this.overview,
        poster_path = this.poster_path,
        rating = this.rating,
        category = category
    )
}

fun MovieEntity.toDomain(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview,
        poster_path = this.poster_path,
        rating = this.rating
    )
}