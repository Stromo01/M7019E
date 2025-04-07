package com.example.m7019e
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.example.m7019e.ui.theme.M7019ETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            M7019ETheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ListMovies()
                }
            }
        }
    }
}


@Composable
fun ListMovies() {
    val movies = listOf("Movie 1", "Movie 2", "Movie 3", "Movie 4", "Movie 5")
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        items(movies) { movie ->
            MovieItem(movie = movie)
        }
    }
}
@Composable
fun MovieItem(movie: String) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.jerry),
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = movie,
            fontSize = 24.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = { /* Do something */ },
            modifier = Modifier.padding(10.dp)
        ) {
            Text(text = "Info")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    M7019ETheme {
        ListMovies()
    }
}