package com.eddy.jedymovieapptest.presentation.screens.filmdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.eddy.jedymovieapptest.R
import com.eddy.jedymovieapptest.domain.models.Film
import com.eddy.jedymovieapptest.presentation.screens.filmdetails.contract.FilmDetailsEffect
import com.eddy.jedymovieapptest.presentation.screens.filmdetails.contract.FilmDetailsIntent
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmDetailsScreen(
    viewModel: FilmDetailsViewModel = koinViewModel(),
    film: Film,
    navigateUp: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.handleIntent(FilmDetailsIntent.GetFilmDetails(film = film))
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                FilmDetailsEffect.NavigateBack -> navigateUp()
            }
        }
    }

    val state = viewModel.state.collectAsState()

    state.value.film?.let { filmDetails ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(R.string.details))
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { viewModel.handleIntent(FilmDetailsIntent.BackClicked) }
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = filmDetails.poster,
                    contentDescription = "Poster Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(bottom = 8.dp)
                )

                Text(
                    text = filmDetails.title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = stringResource(R.string.type),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Text(
                    text = filmDetails.type,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = { viewModel.handleIntent(FilmDetailsIntent.ToggleFavoritesStatus) },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = if (filmDetails.isFavorite)
                            stringResource(R.string.unmark_favorite)
                        else
                            stringResource(R.string.mark_favorite)
                    )
                }
            }
        }
    }
}
