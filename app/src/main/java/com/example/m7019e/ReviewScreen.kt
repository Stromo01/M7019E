package com.example.m7019e

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.m7019e.api.Movie
import com.example.m7019e.api.MovieResponse
import com.example.m7019e.api.Review

@Composable
fun ReviewScreen(navController: NavController, viewModel: MovieViewModel,
                 movieResponse: MovieResponse,) {
    val context = LocalContext.current
    val reviews = viewModel.selectedMovie?.let { movieResponse.getReviews(it) }
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .horizontalScroll(rememberScrollState()),
        ) {
            reviews?.forEach { review ->
                ReviewItem(review)
            }

        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .horizontalScroll(rememberScrollState()),

        ) {
            ExoPlayerView("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
        }
    }
}

@Composable
fun ReviewItem(review: Review) {

    Column(
        modifier = Modifier
            .fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .width(400.dp)
                .height(400.dp)
                .padding(16.dp)
                .padding(top=24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(){
                Text(
                    text = review.author,
                    fontSize = 20.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Left
                )
                Text(
                    text = review.rating.toString(),
                    fontSize = 20.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Left
                )
                Icon(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = null,
                    tint = Color.Yellow,
                    modifier = Modifier
                        .padding(left, 8.dp)
                        .size(24.dp)
                )
            }
            Text(
                text = review.content,
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Left
            )
        }

    }
}

@Composable
fun ExoPlayerView(uri: String) {

    // Get the current context
    val context = LocalContext.current

    // Initialize ExoPlayer
    val exoPlayer = ExoPlayer.Builder(context).build()

    // Create a MediaSource
    val mediaSource = remember(uri) {
        MediaItem.fromUri(uri)
    }

    // Set MediaSource to ExoPlayer
    LaunchedEffect(mediaSource) {
        exoPlayer.setMediaItem(mediaSource)
        exoPlayer.prepare()
    }

    // Manage lifecycle events
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Use AndroidView to embed an Android View (PlayerView) into Compose
    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Set your desired height
    )
}

