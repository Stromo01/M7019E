package com.example.m7019e

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.m7019e.api.Movie
import com.example.m7019e.api.MovieResponse


@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MovieViewModel,
    movieResponse: MovieResponse,
    favMovie: FavoriteMovieHandler,
    initialCategory: String,
    onCategoryChange: (String) -> Unit,
    isNetworkAvailable: Boolean
) {
    var category by remember { mutableStateOf(initialCategory) } // Start with the initial category
    var movies by remember { mutableStateOf(emptyList<Movie>()) }
    LaunchedEffect(category) {
        if (category == "favorites") {
            val favoriteIds = favMovie.getFavoriteMovieIds()
            movies = movieResponse.getFavoriteMovies(favoriteIds)
        } else {
            // Fetch movies for other categories
            val cachedMovies = viewModel.getCachedMoviesByCategory(category).map { it.toDomain() }
            if (cachedMovies.isNotEmpty()) {
                movies = cachedMovies
            } else {
                viewModel.fetchMovies(category, movieResponse)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Banner(currentCategory = category) { selected ->
            category = selected
            onCategoryChange(selected) // Notify the parent of the category change
        }
        DisplayMovies(isNetworkAvailable, movies, navController, viewModel)
    }
}


@Composable
fun Banner(
    currentCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Row( // Banner div design
        modifier = Modifier
            .background(Color(0xFF2c2c2c))
            .fillMaxWidth()
            .padding(top = 45.dp, start = 32.dp, end = 24.dp, bottom = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf("popular", "top_rated", "favorites").forEach { category ->
            Text( // Category text design and color
                text = category.capitalize().replace("_", " "),
                fontSize = 20.sp,
                color = if (currentCategory == category) { // Text color
                    Color.Yellow
                } else {
                    Color.White
                },
                modifier = Modifier.clickable { onCategorySelected(category) }.padding(8.dp), // Text design
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DisplayMovies(isNetworkAvailable: Boolean,
                  movies: List<Movie>,
                  navController: NavController,
                  viewModel: MovieViewModel) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(movies) { movie ->
            MovieItem(isNetworkAvailable ,movie, navController, viewModel)
        }
    }
}

@Composable
fun MovieItem(isNetworkAvailable: Boolean, movie: Movie, navController: NavController, viewModel: MovieViewModel) {
    Column(
        modifier = Modifier
            .padding(8.dp).clickable {
                viewModel.selectedMovie.value = movie
                if(!isNetworkAvailable){
                    Toast.makeText(navController.context, "No Internet Connection", Toast.LENGTH_SHORT).show()
                }
                else {
                    navController.navigate("movie_detail")
                }
            }
            .fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
            contentDescription = null,
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Text(
            text = movie.title,
            fontSize = 24.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${movie.rating}",
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Icon(
                painter = painterResource(id = R.drawable.star),
                contentDescription = null,
                tint = Color.Yellow,
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp)
            )
        }
    }
}