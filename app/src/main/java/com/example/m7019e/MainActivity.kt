package com.example.m7019e
import android.annotation.SuppressLint
import androidx.compose.material3.Surface
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.m7019e.api.Movie
import com.example.m7019e.ui.theme.M7019ETheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.m7019e.api.MovieResponse


class MainActivity : ComponentActivity() {

    private lateinit var favMovie: FavoriteMovieHandler
    private val movieResponse = MovieResponse()
    @SuppressLint("UnrememberedMutableState")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favMovie = FavoriteMovieHandler(this)
        enableEdgeToEdge()
        setContent {
            val movieViewModel: MovieViewModel = viewModel()
            val navController = rememberNavController()

            M7019ETheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavHost(navController = navController, startDestination = "main") {
                        composable("main") {
                            MainScreen(navController, movieViewModel,
                                movieResponse, favMovie)
                        }
                        composable("movie_detail") {
                            MovieDetailScreen(navController, movieViewModel ,
                                movieResponse, favMovie)
                        }
                        composable("review") {
                            ReviewScreen()
                        }
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        val movieViewModel: MovieViewModel = viewModel()
        M7019ETheme {
            val sampleMovies = listOf(
                Movie(
                    id = 1,
                    title = "Sample Movie 1",
                    overview = "",
                    poster_path = "/9cqNxx0GxF0bflZmeSMuL5tnGzr.jpg",
                    rating = 0f,
                    homepage = "",
                    imdbid = ""
                ),
                Movie(
                    id = 2,
                    title = "Sample Movie 2",
                    overview = "",
                    poster_path = "/9cqNxx0GxF0bflZmeSMuL5tnGzr.jpg",
                    rating = 0f,
                    homepage = "",
                    imdbid = ""
                ),
            )
            DisplayMovies(sampleMovies, rememberNavController(),movieViewModel)
            Banner(
                currentCategory = "popular",
                onCategorySelected = { /* Placeholder for preview */ }
            )
        }
    }
}