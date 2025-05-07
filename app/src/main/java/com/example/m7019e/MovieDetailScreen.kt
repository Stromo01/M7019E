package com.example.m7019e

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.m7019e.api.MovieResponse

@Composable
fun MovieDetailScreen(navController: NavController, viewModel: MovieViewModel,
                      movieResponse: MovieResponse, favMovie: FavoriteMovieHandler) {
    val context = LocalContext.current
    val selectedMovie = viewModel.selectedMovie.value // Observe the LiveData

    selectedMovie?.let { movie ->
        val movieDetails = movieResponse.getMovieDetails(movie)

        movieDetails?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = it.title, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${it.poster_path}",
                contentDescription = null,
                modifier = Modifier
                    .height(400.dp)
                    .fillMaxWidth(),

                )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it.genre_names,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 16.sp,
                color = Color.Black,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it.overview,
                fontSize = 16.sp,
                color = Color.DarkGray,
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Heart Icon
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
                    .size(64.dp)
                    .padding(2.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.heart),
                    contentDescription = null,
                    tint = if (isFavorite.value) Color.Red else Color.Gray,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Pushes the buttons to the bottom

            // Buttons Column with Dividers
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (it.homepage.isNotEmpty()) {
                            val uri = Uri.parse(it.homepage)
                            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                                addCategory(Intent.CATEGORY_BROWSABLE)
                            }
                            val chooser = Intent.createChooser(intent, "Open with")
                            context.startActivity(chooser)
                        } else {
                            Toast.makeText(context, "No homepage available", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFf5c518),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Homepage", fontSize = 16.sp)
                }
                Divider(color = Color.Black, thickness = 1.dp)
                Button(
                    onClick = {
                        if (it.imdbid.isNotEmpty()) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.imdb.com/title/${it.imdbid}"))
                            val chooser = Intent.createChooser(intent, "Open with")
                            context.startActivity(chooser)
                        } else {
                            Toast.makeText(context, "No IMDB page available", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFf5c518),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "IMDB", fontSize = 16.sp)
                }
                Divider(color = Color.Black, thickness = 1.dp)
                Button(
                    onClick = {
                        navController.navigate("review")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFf5c518),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Review", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}}