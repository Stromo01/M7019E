package com.example.m7019e

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.m7019e.api.Movie

class MovieViewModel : ViewModel() {
    var selectedMovie by mutableStateOf<Movie?>(null)
}