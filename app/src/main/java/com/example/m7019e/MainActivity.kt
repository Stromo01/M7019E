package com.example.m7019e
import android.annotation.SuppressLint
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.Coil
import coil.compose.AsyncImage
import com.example.m7019e.api.Movie
import com.example.m7019e.api.getMovies
import com.example.m7019e.ui.theme.M7019ETheme


class MainActivity : ComponentActivity() {
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            M7019ETheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val movies = androidx.compose.runtime.mutableStateOf(listOf<Movie>())
                    movies.value= getMovies()
                    ListMovies(movies = movies.value)
                }
            }
        }
    }


    @Composable
    fun ListMovies(movies: List<Movie>) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(movies) { movie ->
                MovieItem(movie = movie)
            }
        }
    }

    @Composable
    fun MovieItem(movie: Movie) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp),
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
            Button(
                onClick = { /* Do something */ },
                modifier = Modifier.padding(10.dp)
            ) {
                Text(text = "Info")
            }
            Text(
                text = "${movie.rating}",
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Image(
                painter = painterResource(id = R.drawable.star),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp),
            )
        }
        }

    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        M7019ETheme {
            val sampleMovies = listOf(
                Movie(id = 1, title = "Sample Movie 1", overview = "", poster_path = "", rating = 0f),
                Movie(id = 2, title = "Sample Movie 2", overview = "", poster_path = "", rating = 0f),
            )
            ListMovies(movies = sampleMovies)
        }
    }
}