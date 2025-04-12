package com.example.m7019e
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.m7019e.api.Movie
import com.example.m7019e.ui.theme.M7019ETheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.m7019e.FavoriteMovieHandler
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.ui.platform.LocalContext
import com.example.m7019e.api.MovieResponse


class MainActivity : ComponentActivity() {

    private lateinit var favMovie: FavoriteMovieHandler
    private val movieRespone = MovieResponse()
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
                            MainScreen(navController, movieViewModel)
                        }
                        composable("movie_detail") {
                            MovieDetailScreen(movieViewModel)
                        }
                    }
                }
            }
        }
    }

 // @Composable
 // fun AppNavigator(movieViewModel: MovieViewModel) {
 //     val navController = rememberNavController()

 //     NavHost(navController = navController, startDestination = "main_screen") {
 //         composable("main_screen") {
 //             MovieDetailScreen(movieViewModel)
 //         }
 //         composable(
 //             "movie_detail/{movieTitle}",
 //             arguments = listOf(navArgument("movieTitle") { type = NavType.StringType })
 //         ) { backStackEntry ->
 //             val movieTitle = backStackEntry.arguments?.getString("movieTitle") ?: ""
 //             MovieDetailScreen(movie Movie,movieTitle)
 //         }
 //     }
 // }


    @Composable
    fun MainScreen(navController: NavHostController, viewModel: MovieViewModel) {
        var category by remember { mutableStateOf("popular") }

        val movies: List<Movie> = if (category == "favorites") {
            movieRespone.getFavoriteMovies(favMovie.getFavoriteMovieIds()) // Fetches favorite movies using the IDs
        } else {
            movieRespone.getMovies(category) // Fetches movies based on the selected category
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Banner(currentCategory = category) { selected ->    // Gets the selected category
                category = selected
            }
            DisplayMovies(movies, navController, viewModel)
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
    fun DisplayMovies( movies: List<Movie>,
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
                MovieItem(movie, navController, viewModel)
            }
        }
    }

    @Composable
    fun MovieItem(movie: Movie, navController: NavController, viewModel: MovieViewModel) {
        Column(
            modifier = Modifier
                .padding(8.dp).clickable {
                    viewModel.selectedMovie = movie
                    navController.navigate("movie_detail")
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
    @Composable
    fun MovieDetailScreen(viewModel: MovieViewModel) {
        val movie = viewModel.selectedMovie?.let { movieRespone.getMovieDetails(it) }

        movie?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = it.title, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${it.poster_path}",
                    contentDescription = null,
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = it.overview,
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(16.dp))
                val isFavorite = remember { mutableStateOf(favMovie.isFavorite(it.id.toString())) }
                IconButton(
                    onClick = {
                        isFavorite.value = !isFavorite.value
                        if (isFavorite.value) {
                            favMovie.addFavoriteMovie(it.id.toString())
                        } else {
                            favMovie.removeFavoriteMovie(it.id.toString())
                        }
                    },
                    modifier = Modifier
                        .size(64.dp) // Increase the size of the IconButton
                        .padding(2.dp) // Add padding to prevent clipping
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.heart),
                        contentDescription = null,
                        tint = if (isFavorite.value) Color.Red else Color.Gray,
                        modifier = Modifier.size(48.dp) // Ensure the Icon is smaller than the IconButton
                    )
                }
                Row(){
                    val context = LocalContext.current
                    Button(
                        onClick = {
                            if (it.homepage.isNotEmpty()) {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.homepage))
                                val chooser = Intent.createChooser(intent, "Open with")
                                context.startActivity(chooser)
                            }
                            else{
                                Toast.makeText(context, "No homepage available", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .height(50.dp)
                    ) {
                        Text(text = "Homepage", fontSize = 16.sp)
                    }
                    Button(
                        onClick = {
                            if (it.imdbid.isNotEmpty()) {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.imdb.com/title/${it.imdbid}"))
                                val chooser = Intent.createChooser(intent, "Open with")
                                context.startActivity(chooser)
                            }
                            else{
                                Toast.makeText(context, "No IMDB page available", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .height(50.dp)
                    ) {
                        Text(text = "IMDB", fontSize = 16.sp)
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