package com.example.m7019e

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.m7019e.api.Movie
import com.example.m7019e.api.MovieResponse
import com.example.m7019e.ui.theme.M7019ETheme

class MainActivity : ComponentActivity() {

    private lateinit var favMovie: FavoriteMovieHandler
    private lateinit var networkConnectionHandler: NetworkConnectionHandler
    private val movieResponse = MovieResponse()
    private var isNetworkAvailable by mutableStateOf(true) // Make it a property of the class

    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize FavoriteMovieHandler
        favMovie = FavoriteMovieHandler(this)

        // Check for network availability
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        isNetworkAvailable = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        // Initialize NetworkConnectionHandler to listen for changes
        networkConnectionHandler = NetworkConnectionHandler(
            context = this,
            onNetworkAvailable = { isNetworkAvailable = true },
            onNetworkLost = { isNetworkAvailable = false }
        )
        networkConnectionHandler.startListening()

        // Set up the UI based on network availability
        setupUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        networkConnectionHandler.stopListening()
    }

    private fun setupUI() {
        enableEdgeToEdge()
        setContent {
            val movieViewModel: MovieViewModel = viewModel()
            val navController = rememberNavController()
            var currentCategory by remember { mutableStateOf("popular") } // Track the current category

            M7019ETheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    if (isNetworkAvailable) {
                        NavHost(navController = navController, startDestination = "main") {
                            composable("main") {
                                MainScreen(
                                    navController = navController,
                                    viewModel = movieViewModel,
                                    movieResponse = movieResponse,
                                    favMovie = favMovie,
                                    initialCategory = currentCategory, // Pass the current category
                                    onCategoryChange = { selectedCategory ->
                                        currentCategory = selectedCategory // Update the category
                                    }
                                )
                            }
                            composable("movie_detail") {
                                MovieDetailScreen(
                                    navController = navController,
                                    viewModel = movieViewModel,
                                    movieResponse = movieResponse,
                                    favMovie = favMovie
                                )
                            }
                            composable("review") {
                                ReviewScreen(
                                    navController = navController,
                                    viewModel = movieViewModel,
                                    movieResponse = movieResponse
                                )
                            }
                        }
                    } else {
                        NoInternetScreen(
                            currentCategory = currentCategory, // Pass the current category
                            onCategorySelected = { selectedCategory ->
                                currentCategory = selectedCategory // Update the category
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun NoInternetScreen(
        currentCategory: String,
        onCategorySelected: (String) -> Unit
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Navigation bar
            Banner(currentCategory = currentCategory, onCategorySelected = onCategorySelected)

            // No internet icon
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.no_wifi),
                    contentDescription = "No Internet",
                    tint = Color.Gray,
                    modifier = Modifier.fillMaxSize(0.3f)
                )
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
            DisplayMovies(sampleMovies, rememberNavController(), movieViewModel)
            Banner(
                currentCategory = "popular",
                onCategorySelected = { /* Placeholder for preview */ }
            )
        }
    }
}
